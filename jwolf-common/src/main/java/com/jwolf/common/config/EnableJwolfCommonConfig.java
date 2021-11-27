package com.jwolf.common.config;

import com.jwolf.common.config.apidoc.EnableSwagger3ApiDoc;
import com.jwolf.common.config.mybatisplus.EnableMybatisPlus;
import com.jwolf.common.config.redis.EnableRedisTemplate;
import com.jwolf.common.config.restTemplate.EnableRestTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * Description: TODO
 * </p>
 *
 * @author majun
 * @version 1.0
 * @date 2019-07-29 23:09
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
//@EnablePermissionAspect 日志与权限在网关统一实现
//@EnableLogAspect
@EnableRestTemplate
@EnableWebMvc
@EnableSwagger3ApiDoc
@EnableMybatisPlus
@EnableRedisTemplate
public @interface EnableJwolfCommonConfig {
} 