package com.jwolf.jwolfauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;

/**
 * 认证授权服务器
 *
 * @author majun
 * @version 1.0
 * @date 2021-11-21 20:59
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})//暂时保留DB 与Redis，后期很可能用
@EnableAuthorizationServer
@EnableWebSecurity
@EnableDiscoveryClient
public class JwolfAuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(JwolfAuthApplication.class, args);
    }
}