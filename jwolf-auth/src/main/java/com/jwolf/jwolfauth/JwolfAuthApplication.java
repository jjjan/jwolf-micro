package com.jwolf.jwolfauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;

/**
 * 认证授权服务器
 *
 * @author majun
 * @version 1.0
 * @date 2021-11-21 20:59
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, RedisAutoConfiguration.class})
@EnableAuthorizationServer
@EnableWebSecurity
public class JwolfAuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(JwolfAuthApplication.class, args);
    }
}