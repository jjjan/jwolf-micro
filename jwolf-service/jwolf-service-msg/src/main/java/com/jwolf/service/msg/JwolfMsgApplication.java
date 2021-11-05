package com.jwolf.service.msg;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@MapperScan("com.jwolf.service.msg.mapper")
@EnableDiscoveryClient(autoRegister = false) //暂不启用nacos
public class JwolfMsgApplication {

    public static void main(String[] args) {

        SpringApplication.run(JwolfMsgApplication.class, args);
    }


}
