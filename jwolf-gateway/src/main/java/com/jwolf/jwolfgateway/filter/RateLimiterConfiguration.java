package com.jwolf.jwolfgateway.filter;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import reactor.core.publisher.Mono;

@Configuration
public class RateLimiterConfiguration {
    /**
     * 用户可通过自定义KeyResolver设置限流维度，例如：
     * <p>
     * 对请求的目标URL进行限流
     * 对来源IP进行限流
     * 通过jwt token等请求头对用户进行限流
     *
     * @return
     */
    @Bean(value = "ipKeyResolver")
    public KeyResolver ipKeyResolver() {
        return exchange -> Mono.just(exchange.getRequest().getRemoteAddress().getAddress().getHostAddress());
    }

    @Bean(value = "userTokenResolver")
    KeyResolver userTokenResolver() {
        return exchange -> Mono.just(exchange.getRequest().getHeaders().getFirst("token"));
    }

    @Bean(value = "apiKeyResolver")
    @Primary
    KeyResolver apiKeyResolver() {
        return exchange -> Mono.just(exchange.getRequest().getPath().value());
    }


    /*@Bean
    public R setRequestRateLimiterGatewayFilterFactory(){
       RequestRateLimiterGatewayFilterFactory RedisRateLimiter rateLimiter = new RedisRateLimiter(1, 1);
        return new RequestRateLimiterGatewayFilterFactory(rateLimiter,apiKeyResolver());
    }*/
}