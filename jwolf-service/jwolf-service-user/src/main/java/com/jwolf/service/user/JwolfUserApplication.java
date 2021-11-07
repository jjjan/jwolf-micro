package com.jwolf.service.user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication(exclude = RedisAutoConfiguration.class)//不启用redis
@EnableSwagger2
@MapperScan("com.jwolf.service.user.mapper")
@EnableDiscoveryClient(autoRegister = false) //暂不启用nacos
public class JwolfUserApplication {

    public static void main(String[] args) {

        SpringApplication.run(JwolfUserApplication.class, args);
    }


}
