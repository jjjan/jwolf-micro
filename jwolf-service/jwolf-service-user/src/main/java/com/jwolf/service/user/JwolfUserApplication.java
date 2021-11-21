package com.jwolf.service.user;

import com.jwolf.common.config.redis.EnableRedisCustomSerilizer;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import springfox.documentation.oas.annotations.EnableOpenApi;

@SpringBootApplication
@EnableOpenApi //EnableSwagger2
@MapperScan("com.jwolf.service.user.mapper")
@EnableDiscoveryClient(autoRegister = true)
@EnableRedisCustomSerilizer
//@EnableGlobalMethodSecurity(prePostEnabled = true)
//@EnableOAuth2Sso
public class JwolfUserApplication {

    public static void main(String[] args) {

        SpringApplication.run(JwolfUserApplication.class, args);
    }


}
