package com.jwolf.jwolfgateway.oauth;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Objects;

/**
 * 鉴权管理器，用于判断是否有资源的访问权限
 * Created by macro on 2020/6/19.
 */
@Slf4j
@Component
public class CustomReactiveAuthorizationManager implements ReactiveAuthorizationManager<AuthorizationContext> {
     /**
     * 此处保存的是资源对应的权限，可以从数据库中获取
     */
    private static final Map<String, String> AUTH_MAP = Maps.newConcurrentMap();

    @PostConstruct
    public void initAuthMap() {
        AUTH_MAP.put("/jwolf/user/user/detail1", "ROLE_ADMIN");
        AUTH_MAP.put("/jwolf/user/user/detail2", "ROLE_ADMIN");
        AUTH_MAP.put("/jwolf/user/user/page", "ROLE_ADMIN");
        AUTH_MAP.put("/user/addUser", "ROLE_ADMIN");
    }


    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> authentication, AuthorizationContext authorizationContext) {
        ServerWebExchange exchange = authorizationContext.getExchange();
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        // 带通配符的可以使用这个进行匹配
        PathMatcher pathMatcher = new AntPathMatcher();
        String authorities = AUTH_MAP.get(path);
        log.info("访问路径:[{}],所需要的权限是:[{}]", path, authorities);

        // option 请求，全部放行
        if (request.getMethod() == HttpMethod.OPTIONS) {
            return Mono.just(new AuthorizationDecision(true));
        }

        // 不在权限范围内的url，全部拒绝
        if (!StringUtils.hasText(authorities)) {
            return Mono.just(new AuthorizationDecision(false));
        }

        return authentication
                .filter(Authentication::isAuthenticated)
                .filter(a -> a instanceof JwtAuthenticationToken)
                .cast(JwtAuthenticationToken.class)
                .doOnNext(token -> {
                    System.out.println(token.getToken().getHeaders());
                    System.out.println(token.getTokenAttributes());
                })
                .flatMapIterable(AbstractAuthenticationToken::getAuthorities)
                .map(GrantedAuthority::getAuthority)
                .any(authority -> Objects.equals(authority, authorities))
                .map(AuthorizationDecision::new)
                .defaultIfEmpty(new AuthorizationDecision(false));
    }


}