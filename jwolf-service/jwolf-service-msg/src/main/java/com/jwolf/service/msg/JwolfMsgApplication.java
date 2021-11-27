package com.jwolf.service.msg;

import com.jwolf.common.config.EnableJwolfCommonConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import springfox.documentation.oas.annotations.EnableOpenApi;

@SpringBootApplication(scanBasePackages = "com.jwolf.service")
@MapperScan("com.jwolf.service.msg.mapper")
@EnableDiscoveryClient
@EnableFeignClients(basePackages ="com.jwolf.service")
@EnableJwolfCommonConfig
public class JwolfMsgApplication {

    public static void main(String[] args) {

        SpringApplication.run(JwolfMsgApplication.class, args);
    }


}
