package com.jwolf.jwolfmanage.config.globleexception;

import cn.hutool.core.collection.CollectionUtil;
import com.jwolf.common.bean.ResultEntity;
import com.jwolf.common.constant.ErrorEnum;
import com.jwolf.common.exception.CommonException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
* <p>Title: 统一异常处理器</p>
* <p>Description:处理自定义异常，参数验证@Valid异常及其它未知异常</p>
* @author  majun
* @date   2018/6/28 9:37
*/

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @Value("${spring.profiles.active}")
    private String profile;

    @ExceptionHandler(value = CommonException.class)
    public Object CommonException(CommonException e, HttpServletResponse response) {
        log.error("统一异常处理器捕获【自定义commonException异常】-->" + e.getErrorMsg(), e);
        return ResultEntity.fail(e.getErrorCode(), e.getErrorMsg());
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public Object IllegalArgumentExceptionHandler(IllegalArgumentException e, HttpServletResponse response) {
        log.error("统一异常处理器捕获【spring Assert断言异常】" ,e);
       return ResultEntity.fail(e.getMessage());
    }

    /**
     * JSON传参时校验失败时会抛出MethodArgumentNotValidException异常
     * form-data传参教研失败时会抛出BindException
     * @param e
     * @return
     */
    @ExceptionHandler(value = {MethodArgumentNotValidException.class, BindException.class})
    public Object MethodArgumentNotValidHandler(Exception e) {
        List<String> errorMsgList = new ArrayList<>();
        if (e instanceof MethodArgumentNotValidException) {
            ((MethodArgumentNotValidException) e).getBindingResult().getFieldErrors().forEach(error -> errorMsgList.add(error.getDefaultMessage()));
        } else  {
            ((BindException) e).getFieldErrors().forEach(error -> errorMsgList.add(error.getDefaultMessage()));
        }
        log.error("统一异常处理器捕获【参数验证不通过异常】-->" + errorMsgList);
        return ResultEntity.fail("0400", CollectionUtil.join(errorMsgList,","));
    }

    @ExceptionHandler(value = Exception.class)
    //@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR,reason = "内部异常")//加了该注解，浏览器显示HttpStatus状态码，返回的不再是自定义状态码
    public Object OtherExceptionHandler(Exception e, HttpServletResponse response, HttpServletRequest request) {
        log.error("统一异常处理器捕获{}【未知异常】{},详情:{}" ,request.getRequestURI(), e.getMessage(), e);
        return profile.equals("prod")? ResultEntity.fail(ErrorEnum.UNKNOWN_ERROR.getMsg()):ResultEntity.fail(e.toString());
    }
}