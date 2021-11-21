package com.jwolf.jwolfauth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
    
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
        endpoints.tokenStore(jwtTokenStore()).accessTokenConverter(jwtAccessTokenConverter());
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
