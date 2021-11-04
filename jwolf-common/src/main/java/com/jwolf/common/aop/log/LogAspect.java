package com.jwolf.common.aop.log;

import com.jwolf.common.base.entity.ResultEntity;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.jwolf.common.config.CommonAutoRefreshConfig;
import com.jwolf.common.exception.CommonException;
import io.micrometer.core.instrument.ImmutableTag;
import io.micrometer.core.instrument.Tag;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author jwolf
 * @description:
 */
@Aspect
@Slf4j
public class LogAspect {

    private static List<String> METHOD_LIST = Lists.newArrayList("PUT", "POST");
    @Value("${spring.application.name}")
    private String applicationName; // 微服务名

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;
    @Autowired
    private PrometheusMeterRegistry registry;
    @Autowired
    private HttpServletRequest req;

    @Autowired
    private CommonAutoRefreshConfig refreshConfig;
    private List<LogBuilder> tempLogStore = new ArrayList<>();
    private ThreadLocal<LogBuilder> aopLogBuilder = new ThreadLocal<>();
    private Lock lock = new ReentrantLock();

    @Pointcut("execution(public * com.jwolf.*.controller.*.*(..))")
    public void webLog() {
    }

    @Before("webLog()")
    public void deBefore(JoinPoint joinPoint) {
    }

    @AfterReturning(returning = "ret", pointcut = "webLog()")
    public void doAfterReturning(Object ret) {
        //正常执行打印日志及入库
        processLog(aopLogBuilder.get());
    }

    /**
     * 后置异常通知
     */
    @AfterThrowing(value = "webLog()", throwing = "e")
    public void throwss(JoinPoint jp, Exception e) {
        LogBuilder builder = aopLogBuilder.get();
        //设置错误信息
        builder.setCost(System.currentTimeMillis() - builder.getReqTime());
        if (e instanceof CommonException) {
            CommonException commonException = (CommonException) e;
            builder.setMsg(commonException.getErrorMsg()).setCode(commonException.getErrorCode());

        } else if (e instanceof MethodArgumentNotValidException) {
            builder.setMsg("");
        } else if (e instanceof IllegalArgumentException) {
            builder.setMsg(e.getMessage()).setCode("");
        } else {
            builder.setMsg("");
        }
        //错误执行打印日志
        processLog(aopLogBuilder.get());
    }

    /**
     * 后置最终通知,final增强，不管是抛出异常或者正常退出都会执行
     */
    @After("webLog()")
    public void after(JoinPoint jp) throws NoSuchMethodException {
        LogBuilder builder = aopLogBuilder.get();
        //接口描述
        Class clazz = jp.getTarget().getClass();
        String methodName = jp.getSignature().getName();
        MethodSignature signature = (MethodSignature) (jp.getSignature());
        Class<?>[] parameterTypes = signature.getMethod().getParameterTypes();
        Method method = clazz.getMethod(methodName, parameterTypes);
        //其它请求信息
        builder.setApp(applicationName)
                .setMethod(req.getMethod())
                .setIp(req.getHeader("x_forwarded_ip"))
                .setParams(getReqParams(jp))
                .setCost(System.currentTimeMillis() - builder.getReqTime())
                .setUserId(builder.getUserId() == null ? req.getHeader("zuul_user_id") : builder.getUserId())
                .setAccessType(builder.getAccessType() == null ? req.getHeader("zuul_access_type") : builder.getAccessType());

    }


    /**
     * 环绕通知,环绕增强，相当于MethodInterceptor
     */
    @Around("webLog()")
    public Object arround(ProceedingJoinPoint pjp) throws Throwable {
        LogBuilder builder = new LogBuilder().setReqTime(System.currentTimeMillis());
        builder.setUri(req.getRequestURI());
        aopLogBuilder.set(builder);
        Object obj = pjp.proceed();
        //正常执行设置返回信息
        if (obj instanceof ResultEntity) {
            ResultEntity result = (ResultEntity) obj;
            builder.setCode(result.getCode());
            String jsonResult = JSON.toJSONString("");
            builder.setData(jsonResult.length() > refreshConfig.getRetDataPrintLen() ? jsonResult.substring(0, refreshConfig.getRetDataPrintLen()) : jsonResult);

        } else {
            builder.setMsg("warning！非标准ResultEntity正常返回");
            String jsonResult = JSON.toJSONString(obj);
            builder.setData(jsonResult.length() > refreshConfig.getRetDataPrintLen() ? jsonResult.substring(0, refreshConfig.getRetDataPrintLen()) : jsonResult);
        }
        return obj;

    }


    /**
     * 日志打印及入库
     *
     * @param logBuilder
     */
    private void processLog(LogBuilder logBuilder) {
        //入库MySQL及上传prometheus metric
        asyncUploadMetricAndStoreBatchLog(logBuilder);

    }

    private void asyncUploadMetricAndStoreBatchLog(LogBuilder logBuilder) {
        taskExecutor.execute(() -> {
            //上传prometheus metric
            collectPrometheusMetric(logBuilder);

        });

    }

    /**
     * PUT/POST请求参数只能从joinPoint获取
     *
     * @param joinPoint
     * @return
     */
    private String getReqParams(JoinPoint joinPoint) {
        String reqParams = "";
        if (!METHOD_LIST.contains(req.getMethod())) {
            reqParams = JSON.toJSONString(req.getParameterMap());
        } else {
            for (Object arg : joinPoint.getArgs()) {
                if (arg instanceof HttpServletRequest
                        || arg instanceof HttpServletResponse
                        || arg instanceof MultipartFile) {
                    continue;
                }
                reqParams += JSON.toJSONString(arg) + ";";
            }
        }

        if (reqParams.length() > refreshConfig.getReqParamsPrintLen()) { // base64图片上传等
            return reqParams.substring(0, refreshConfig.getReqParamsPrintLen()) + "...";
        }
        return reqParams;
    }

    /**
     * prometheus metric采集
     */
    private void collectPrometheusMetric(LogBuilder logBuilder) {
        Tag tag1 = new ImmutableTag("uri", logBuilder.getUri());
        Tag tag2 = new ImmutableTag("code", logBuilder.getCode());
        Tag tag3 = new ImmutableTag("accessType", logBuilder.getAccessType());
        registry.timer("api_cost_timer", Lists.newArrayList(tag1, tag2, tag3)).record(logBuilder.getCost(), TimeUnit.MILLISECONDS);
    }
}
