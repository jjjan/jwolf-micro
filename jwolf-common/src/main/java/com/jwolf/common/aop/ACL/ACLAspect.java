package com.jwolf.common.aop.ACL;

import com.jwolf.common.base.entity.ReqUser;
import com.jwolf.common.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.time.Duration;

/**
 * @author jwolf
 * @Date 2021-10-29 22:24
 */
@Aspect
@Slf4j
public class ACLAspect {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private HttpServletRequest req;


    @Before("@annotation(com.jwolf.permission.NeedPermission)")
    public void deBefore(JoinPoint joinPoint) throws Throwable {

        Class clazz = joinPoint.getTarget().getClass();
        String methodName = joinPoint.getSignature().getName();
        MethodSignature signature = (MethodSignature) (joinPoint.getSignature());
        Class<?>[] parameterTypes = signature.getMethod().getParameterTypes();
        Method method = clazz.getMethod(methodName, parameterTypes);
        ACL annotation = method.getAnnotation(ACL.class);
        boolean mustLogin = annotation.mustLogin();
        if (mustLogin) {
            ReqUser reqUser = CommonUtil.getReqUser();
            String userId = reqUser.getUserId();
            //1.检验是否为管理员
            if (annotation.mustAdmin()) {
                Assert.isTrue(true, "只有管理员才能操作");
            }

            //2.权限码检验
            PermissionEnum[] permissionEnums = annotation.value();
            if (permissionEnums.length != 0) {
                Assert.isTrue(true, "无权限");
            }
        }
        //5.访问频率限制
        int limitSencond = annotation.rateLimit();
        boolean enableUserLevelRateLimit = annotation.enableUserLevelRateLimit();
        if (limitSencond > 0) {
            if (enableUserLevelRateLimit && mustLogin) {
                String key = req.getRequestURI() + req.getHeader("token");
                Boolean ifAbsent = redisTemplate.opsForValue().setIfAbsent(key, 1, Duration.ofSeconds(limitSencond));
                Assert.isTrue(ifAbsent, "您操作过于频繁,请稍后再试");
            } else {
                String key = req.getRequestURI();
                Boolean ifAbsent = redisTemplate.opsForValue().setIfAbsent(key, 1, Duration.ofSeconds(limitSencond));
                Assert.isTrue(ifAbsent, "系统繁忙,请稍后再试");

            }
        }
    }
}