package com.jwolf.service.user;

import com.jwolf.common.config.EnableJwolfCommonConfig;
import com.jwolf.common.config.redis.EnableRedisTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import springfox.documentation.oas.annotations.EnableOpenApi;

@SpringBootApplication
@MapperScan("com.jwolf.service.user.mapper")
@EnableDiscoveryClient(autoRegister = true)
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableOAuth2Sso
@EnableJwolfCommonConfig
public class JwolfUserApplication {

    public static void main(String[] args) {

        SpringApplication.run(JwolfUserApplication.class, args);
    }


}
