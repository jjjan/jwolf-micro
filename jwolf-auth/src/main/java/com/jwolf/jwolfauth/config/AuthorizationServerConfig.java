package com.jwolf.jwolfauth.config;

import cn.hutool.core.map.MapBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Configuration
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
    @Autowired
    private UserDetailsService userDetailsService;
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("client1")
                .secret(passwordEncoder().encode("secret1"))
                .authorizedGrantTypes("authorization_code")
                .scopes("all")
                //.accessTokenValiditySeconds(10)
                //.refreshTokenValiditySeconds(864000)
                .redirectUris("http://baidu.com","http://localhost:8881/login") // 授权成功后运行跳转的url，sso客户端默认/login，可在client端通过security.oauth2.sso.login-path修改为其它
                .autoApprove(false)  // true则自动授权,跳过授权页面点击步骤
                .and()
                .withClient("client2")
                .secret(passwordEncoder().encode("secret2"))
                .authorizedGrantTypes("authorization_code")
                .scopes("all")
                //.accessTokenValiditySeconds(10)
                //.refreshTokenValiditySeconds(864000)
                .redirectUris("http://localhost:9602/login")
                .autoApprove(false);

    }
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints)  {
        //endpoints.tokenStore(jwtTokenStore()).accessTokenConverter(jwtAccessTokenConverter());
       TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        List<TokenEnhancer> delegates = new ArrayList<>();
        delegates.add((oAuth2AccessToken, oAuth2Authentication) -> {
            Map<String, Object> infoMap = MapBuilder.<String, Object>create().put("info1", "123").build();
            ((DefaultOAuth2AccessToken) oAuth2AccessToken).setAdditionalInformation(infoMap);
            return oAuth2AccessToken;
        });
        //注意jwtAccessTokenConverter一定要在其它增强链最后，它的作用是生成jwt字符串
        delegates.add(jwtAccessTokenConverter());
        tokenEnhancerChain.setTokenEnhancers(delegates);
        endpoints.tokenStore(jwtTokenStore())
                .userDetailsService(userDetailsService)
                .tokenEnhancer(tokenEnhancerChain);

    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security)  {
        // 要访问认证服务器tokenKey的时候需要经过身份认证
        security.tokenKeyAccess("isAuthenticated()");
    }

    /**
     * 可以使用jdbc,redis,jwt,memory等方式存储token
     * @return
     */
    @Bean
    public TokenStore jwtTokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        // JWT签名用的key，泄漏会导致JWT被伪造
        converter.setSigningKey("dev");
        return converter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
