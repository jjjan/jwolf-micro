package com.jwolf.service.msg.config;//实现feign的请求拦截器并在feign调用配置，@FeignClient(name = "capability-register", fallback = ApiServiceClientFallBack.class ,configuration = XXXXConfiguration.class)


import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Configuration
public class FeignRequestInterceptor implements RequestInterceptor {
 
    @Override
    public void apply(RequestTemplate requestTemplate) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        requestTemplate.header("Token", request.getHeader("Token"));
    }
}


