package com.jwolf.common.config.apidoc;

import com.jwolf.common.config.restTemplate.RestTemplateConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;

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
@ImportAutoConfiguration(SwaggerConfiguration.class)
public @interface EnableSwagger3ApiDoc {
}