package com.jwolf.service.user;

import com.jwolf.common.config.EnableJwolfCommonConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@MapperScan("com.jwolf.service.user.mapper")
@EnableDiscoveryClient(autoRegister = true)
@EnableJwolfCommonConfig
public class JwolfUserApplication {

    public static void main(String[] args) {

        SpringApplication.run(JwolfUserApplication.class, args);
    }


}
