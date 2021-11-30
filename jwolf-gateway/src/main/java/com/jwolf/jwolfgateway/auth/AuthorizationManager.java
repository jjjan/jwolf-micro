package com.jwolf.jwolfgateway.auth;

import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 鉴权管理器
 */
@Component
@AllArgsConstructor
@Slf4j
public class AuthorizationManager implements ReactiveAuthorizationManager<AuthorizationContext> {


    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> mono, AuthorizationContext authorizationContext) {
        ServerHttpRequest request = authorizationContext.getExchange().getRequest();
        String path = request.getURI().getPath();
        PathMatcher pathMatcher = new AntPathMatcher();
        //token为空拒绝访问
        mono.map(Authentication::getAuthorities).doOnNext(grantedAuthorities -> System.out.println(grantedAuthorities));
        String token = request.getHeaders().getFirst("token");
        List<String> accessToken = request.getQueryParams().get("access_token");
        String jwtAccessToken = accessToken == null ? null : accessToken.get(0);
        if (StrUtil.isBlank(token) && StrUtil.isBlank(jwtAccessToken)) {
            // 前端根据状态码location.href=lcoalhost:9402/mylogin,登陆后走auth的/to-app接口携带token跳到前端首页
            return Mono.just(new AuthorizationDecision(false));
        }
        //如下参考的判断逻辑暂未调通？考虑自己token解析到用户信息，从redis取出该请求path对应的roleids,判断用户的角色id是否包含在内
        Mono<AuthorizationDecision> authorizationDecisionMono = mono
                .filter(Authentication::isAuthenticated)
                .filter(a -> a instanceof JwtAuthenticationToken)
                .cast(JwtAuthenticationToken.class)
                .doOnNext(t -> {
                    System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxx" + t.getToken().getHeaders());
                    System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxx2" + t.getTokenAttributes());
                })
                .flatMapIterable(Authentication::getAuthorities)
                .map(GrantedAuthority::getAuthority)
                .any(roleId -> {
                    // 5. roleId是请求用户的角色(格式:ROLE_{roleId})，authorities是请求资源所需要角色的集合
                    log.info("访问路径：{}", path);
                    log.info("用户角色roleId：{}", roleId);
                    log.info("资源需要权限authorities：{}", "xxx");
                    return true;
                })
                .map(AuthorizationDecision::new)
                .defaultIfEmpty(new AuthorizationDecision(false));
        return authorizationDecisionMono;
    }
}
