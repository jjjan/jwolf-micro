package com.jwolf.common.util;

import com.alibaba.fastjson.JSON;
import com.jwolf.common.base.entity.ReqUser;
import com.jwolf.common.exception.CommonException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author jwolf
 * @Date 2021-10-29 22:24
 */
@Slf4j
public class LoginUserHolder {

   public static ReqUser getCurrentUser(){
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        String userStr = request.getHeader("user");
        return JSON.parseObject(userStr,ReqUser.class);

   }

}