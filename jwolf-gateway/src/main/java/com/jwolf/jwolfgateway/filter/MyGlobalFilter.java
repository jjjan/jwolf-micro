package com.jwolf.jwolfgateway.filter;

import cn.hutool.core.util.StrUtil;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class MyGlobalFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange , GatewayFilterChain chain) {
        System.out.println("全局过滤器执行...");
        //方案1.从header获取token自己解析到用户信息,并将解析后用户信息字段分为多个header向下游传递避免下游再解析？？
        //String token = exchange.getRequest().getHeaders().getFirst("token");
        //ServerHttpRequest request = exchange.getRequest().mutate().header("user", realToken).build();
        //exchange = exchange.mutate().request(request).build();
        //方案2.从安全上下文获取用户信息,并将解析后用户信息字段分为多个header向下游传递避免下游再解析
        ServerWebExchange finalExchange = exchange;
        ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .cast(JwtAuthenticationToken.class)
                .flatMap(authentication -> {
                    ServerHttpRequest request = finalExchange.getRequest();
                    request = request.mutate()
                            .header("token", authentication.getPrincipal().toString())
                            .build();

                    ServerWebExchange newExchange = finalExchange.mutate().request(request).build();

                    return chain.filter(newExchange);
                });


        return chain.filter(exchange);
    }

    // 过滤器执行顺序 return的值越小越先执行
    @Override
    public int getOrder() {
        return 0;
    }
}
