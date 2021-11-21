package com.jwolf.jwolfgateway.filter;
 
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
 
/**
 * 将token信息传递到下游服务中
 *
 * @author huan.fu 2021/8/25 - 下午2:49
 */
public class TokenTransferFilter implements WebFilter {
 
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
 
    static {
        OBJECT_MAPPER.registerModule(new Jdk8Module());
        OBJECT_MAPPER.registerModule(new JavaTimeModule());
    }
 
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .cast(JwtAuthenticationToken.class)
                .flatMap(authentication -> {
                    ServerHttpRequest request = exchange.getRequest();
                    request = request.mutate()
                            .header("tokenInfo", toJson(authentication.getPrincipal()))
                            .build();
 
                    ServerWebExchange newExchange = exchange.mutate().request(request).build();
 
                    return chain.filter(newExchange);
                });
    }
 
    public String toJson(Object obj) {
        try {
            return OBJECT_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}