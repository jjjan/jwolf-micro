package com.jwolf.jwolfgateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import springfox.documentation.oas.annotations.EnableOpenApi;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})//不启用redis
@EnableOpenApi
@EnableDiscoveryClient
public class JwolfGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(JwolfGatewayApplication.class, args);
    }

}
