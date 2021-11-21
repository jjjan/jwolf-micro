package com.jwolf.jwolfgateway.filter;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.jwt.JWT;
import com.alibaba.fastjson.JSON;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

// 过滤器需要成为Spring的组件
//@Component
public class MyGlobalFilter implements GlobalFilter, Ordered {

   @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String token = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (StrUtil.isEmpty(token)) {
            return chain.filter(exchange);
        }
            //从token中解析用户信息并设置到Header中去
            String realToken = token.replace("Bearer ", "");
            JSONObject claimsJson = JWT.create()
                .setSigner(null, "dev".getBytes(StandardCharsets.UTF_8))
                .parse(realToken)
                .getPayload()
                .getClaimsJson();
            ServerHttpRequest request = exchange.getRequest().mutate().header("user", JSON.toJSONString(claimsJson)).build();
            exchange = exchange.mutate().request(request).build();

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }

}
