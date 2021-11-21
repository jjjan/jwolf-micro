package com.jwolf.jwolfgateway.routes;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.factory.RequestRateLimiterGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.RetryGatewayFilterFactory;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

@Configuration
@Slf4j
public class MicroServiceRoute {
    @Autowired
    private RequestRateLimiterGatewayFilterFactory factory;

    @Bean
    public RouteLocator customerRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
               .route("user", r -> r.path("/jwolf/user/**")
                        .and()
                        //.between()
                        //.header("xxx")
                        //.predicate()
                        //.host()
                        //.method(HttpMethod.GET)
                        //.query()
                        //.remoteAddr()
                        //.weight()
                        .after(ZonedDateTime.of(LocalDateTime.now().minusDays(11), ZoneId.systemDefault()))
                        .filters(gatewayFilterSpec -> gatewayFilterSpec
                                .filter((exchange, chain) ->{
                                    log.error(exchange.getRequest().getPath().toString());
                                    return chain.filter(exchange);},1)
                                .filter((exchange, chain) ->{
                                    exchange.getResponse().setStatusCode(HttpStatus.OK);
                                    return chain.filter(exchange);},2)
                                .stripPrefix(1) //去除前缀/jwolf的过滤器

                               )
                        .uri("lb://user")) //暂没使用注册中心，直接转发到具体地址
                        //.uri("http://localhost:8881"))
                .route("travel", r -> r.path("/jwolf/travel/**")
                        .and()
                        .after(ZonedDateTime.of(LocalDateTime.now().minusDays(11), ZoneId.systemDefault()))
                        .filters(gatewayFilterSpec -> gatewayFilterSpec
                                .filter((exchange, chain) ->{
                                    log.error(exchange.getRequest().getPath().toString());
                                    return chain.filter(exchange);},1)
                                .filter((exchange, chain) ->{
                                    exchange.getResponse().setStatusCode(HttpStatus.OK);
                                    return chain.filter(exchange);},2)
                                .stripPrefix(1)
                               )
                        .uri("http://jd.com")) //跳转到京东
                .route("msg", r -> r.path("/jwolf/msg/**")
                        .and()
                        .after(ZonedDateTime.of(LocalDateTime.now().minusDays(11), ZoneId.systemDefault()))
                        .filters(gatewayFilterSpec -> gatewayFilterSpec
                                .filter((exchange, chain) ->{
                                    log.error(exchange.getRequest().getPath().toString());
                                    return chain.filter(exchange);},1)
                                 //转发重试
                               .filter(new RetryGatewayFilterFactory().apply(retryConfig -> {
                                    retryConfig
                                            .setRetries(2).setBackoff(Duration.of(10, ChronoUnit.MILLIS), Duration.of(50, ChronoUnit.MILLIS), 2, false)
                                            .setStatuses(HttpStatus.BAD_GATEWAY, HttpStatus.GATEWAY_TIMEOUT)//超时及路由失败时时重试
                                            .setMethods(HttpMethod.GET, HttpMethod.DELETE);//post/put如有幂等风险建议不重试
                                }))
                                .filter((exchange, chain) ->{
                                    exchange.getResponse().setStatusCode(HttpStatus.OK);
                                    return chain.filter(exchange);},2)
                                .stripPrefix(1)
                               )
                        //.uri("http://localhost:8882")) //暂不启动msg服务，以测试重试机制
                        .uri("lb://msg")) //暂没使用注册中心，直接转发到具体地址
                .build();
    }

}
