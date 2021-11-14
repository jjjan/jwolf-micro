package com.jwolf.jwolfgateway.filter;

import com.alibaba.cloud.commons.lang.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

// 过滤器需要成为Spring的组件
@Component
public class MyGlobalFilter implements GlobalFilter, Ordered {

    // 过滤器逻辑
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        System.out.println("全局过滤器执行...");
        String token = exchange.getRequest().getQueryParams().getFirst("token");
        if (StringUtils.isNotBlank(token))
        {
            // 设置状态码为未授权
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            // 设为已完成 不再继续执行
            return exchange.getResponse().setComplete();
        }
        return chain.filter(exchange);
    }

    // 过滤器执行顺序 return的值越小越先执行
    @Override
    public int getOrder() {
        return 1;
    }
}
