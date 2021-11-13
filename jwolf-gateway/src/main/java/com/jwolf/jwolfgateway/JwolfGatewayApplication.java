package com.jwolf.jwolfgateway;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class})//不启用redis
@EnableOpenApi
@EnableDiscoveryClient(autoRegister = false) //暂不启用nacos
public class JwolfGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(JwolfGatewayApplication.class, args);
    }

}
