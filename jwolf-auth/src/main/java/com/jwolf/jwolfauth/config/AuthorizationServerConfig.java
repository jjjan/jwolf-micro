package com.jwolf.jwolfauth.config;

import cn.hutool.core.map.MapBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.PathResource;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
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
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import java.security.KeyPair;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Configuration
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                //client1为jwolf-manage授权登录的配置
                .withClient("client1")
                .secret(passwordEncoder().encode("secret1"))
                .authorizedGrantTypes("authorization_code", "refresh_token")
                .scopes("all")
                .accessTokenValiditySeconds(100)
                .refreshTokenValiditySeconds(864000)
                .redirectUris("http://localhost:8888/manager-index") // 授权成功后运行跳转的url，sso客户端默认/login，可在client端通过security.oauth2.sso.login-path修改为其它
                .autoApprove(false)  // true则自动授权,跳过授权页面点击步骤
                .and()
                //第三方授权登录时可以再这里追加，如果要做到类似微信授权登录一样，就需要从DB读取client信息。
                //并开放注册页面让用户注册，appId=clientId,appSecret=secret，并要求用户提供回调地址，这正是微信接入申请能拿到的三个核心参数
                //client2暂未使用
                .withClient("client2")
                .secret(passwordEncoder().encode("secret2"))
                .authorizedGrantTypes("refresh_token", "authorization_code")
                .scopes("all")
                .accessTokenValiditySeconds(110)
                .refreshTokenValiditySeconds(864000)
                .redirectUris("http://www.baidu.com")
                .autoApprove(false);

    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        List<TokenEnhancer> delegates = new ArrayList<>();
        delegates.add((oAuth2AccessToken, oAuth2Authentication) -> {
            Map<String, Object> infoMap = MapBuilder.<String, Object>create().put("info1", "123").build();
            ((DefaultOAuth2AccessToken) oAuth2AccessToken).setAdditionalInformation(infoMap);
            return oAuth2AccessToken;
        });
        //注意jwtAccessTokenConverter一定要在其它增强链最后，它的作用是生成jwt字符串，指定加密算法，key等
        delegates.add(jwtAccessTokenConverter());
        tokenEnhancerChain.setTokenEnhancers(delegates);
        endpoints.tokenStore(new JwtTokenStore(jwtAccessTokenConverter()))
                .accessTokenConverter(jwtAccessTokenConverter())
                .authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService)
                .tokenEnhancer(tokenEnhancerChain)
                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST)
                .reuseRefreshTokens(false);


    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        security
                // .tokenKeyAccess("permitAll()")
                // .checkTokenAccess("permitAll()")
                .allowFormAuthenticationForClients();
    }

    /**
     * 使用非对称加密算法对token签名
     */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setKeyPair(keyPair()); //auth与gateway的token使用rsa非对称加密
        converter.setSigningKey("dev");//jwolf-manage 解析token需要该jwt key
        return converter;
    }


    /**
     * 从绝对路径或classpath下的密钥库中获取密钥对(公钥+私钥)
     */
    @Bean
    public KeyPair keyPair() {
        KeyStoreKeyFactory factory = new KeyStoreKeyFactory(new PathResource("c:\\jwolf.jks"), "123456".toCharArray());
        KeyPair keyPair = factory.getKeyPair("jwolf", "123456".toCharArray());
        return keyPair;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
