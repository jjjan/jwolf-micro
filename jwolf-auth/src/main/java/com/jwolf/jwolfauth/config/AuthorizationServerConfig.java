package com.jwolf.jwolfauth.config;

import com.google.common.collect.Lists;
import com.jwolf.jwolfauth.constant.AuthConstant;
import com.jwolf.jwolfauth.core.MemberUser;
import com.jwolf.jwolfauth.core.MemberUserDetailsServiceImpl;
import com.jwolf.jwolfauth.core.SysUser;
import com.jwolf.jwolfauth.core.SysUserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.PathResource;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;

import java.security.KeyPair;
import java.util.*;

@Configuration
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private MemberUserDetailsServiceImpl memberUserDetailsService;
    @Autowired
    private SysUserDetailsServiceImpl sysUserDetailsService;


    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                //client1为jwolf-manage授权登录的配置
                .withClient(AuthConstant.JWOLF_MANAGE_CLIENTID)
                .secret(passwordEncoder().encode("secret1"))
                .authorizedGrantTypes("authorization_code", "refresh_token")
                .scopes("all")
                .accessTokenValiditySeconds(10)
                .refreshTokenValiditySeconds(10)
                .redirectUris("http://localhost:8888/jwolf/manage/me") // 授权成功后运行跳转的url，sso客户端默认/login，可在client端通过security.oauth2.sso.login-path修改为其它
                .autoApprove(true)  // true则自动授权,跳过授权页面点击步骤
                .and()
                //第三方授权登录时可以再这里追加，如果要做到类似微信授权登录一样，就需要从DB读取client信息。
                //并开放注册页面让用户注册，appId=clientId,appSecret=secret，并要求用户提供回调地址，这正是微信接入申请能拿到的三个核心参数
                //从DB查询client信息，implement ClientDetailsService即可，略
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
        List<TokenEnhancer> delegates = Lists.newArrayList(tokenEnhancer());
        //注意jwtAccessTokenConverter一定要在其它增强链最后，它的作用是生成jwt字符串，指定加密算法，key等
        delegates.add(jwtAccessTokenConverter());
        tokenEnhancerChain.setTokenEnhancers(delegates);
        endpoints
                .accessTokenConverter(jwtAccessTokenConverter())
                .authenticationManager(authenticationManager)
                .tokenEnhancer(tokenEnhancerChain)
                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST)
                .reuseRefreshTokens(true)
                .tokenServices(tokenServices(endpoints));


    }

    public DefaultTokenServices tokenServices(AuthorizationServerEndpointsConfigurer endpoints) {
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        List<TokenEnhancer> tokenEnhancers = new ArrayList<>();
        tokenEnhancers.add(tokenEnhancer());
        tokenEnhancers.add(jwtAccessTokenConverter());
        tokenEnhancerChain.setTokenEnhancers(tokenEnhancers);
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setTokenStore(endpoints.getTokenStore());
        tokenServices.setSupportRefreshToken(true);
        tokenServices.setTokenEnhancer(tokenEnhancerChain);
        //tokenServices.setClientDetailsService(clientDetailsService);
        PreAuthenticatedAuthenticationProvider provider = new PreAuthenticatedAuthenticationProvider();
        provider.setPreAuthenticatedUserDetailsService(authenticationToken -> {
            if (AuthConstant.JWOLF_MANAGE_CLIENTID.equals(authenticationToken.getName())) { //jwolf-manger(client1)或其它公司内部其它内部管理系统单点登录的clientId也走系统内部用户认证
                return sysUserDetailsService.loadUserByUsername(authenticationToken.getName());
            } else {//其它公司系统Jwolf授权登录的clientId及jwolf会员用户走这里,当然其它公司要接入jwolf认证授权登录的条件是jwolf有微信，支付宝那样的用户量人家才会集成jwolf第三方登录
                return memberUserDetailsService.loadUserByUsername(authenticationToken.getName());
            }
        });
        tokenServices.setAuthenticationManager(new ProviderManager(Arrays.asList(provider)));
        return tokenServices;

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
        converter.setSigningKey("dev");//jwolf-manage 解析token需要该jwt key？？？ 不设置则报错too many redirect
        return converter;
    }


    /**
     * 从绝对路径或classpath下的密钥库中获取密钥对(公钥+私钥)
     * keytool -genkey -alias jwolf -keyalg RSA -keypass 123456 -keystore jwolf.jks -storepass 123456
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


    /**
     * JWT加入自定义字段
     */
    @Bean
    public TokenEnhancer tokenEnhancer() {
        return (accessToken, authentication) -> {
            Map<String, Object> additionalInfo = new HashMap<>();
            Object principal = authentication.getUserAuthentication().getPrincipal();
            //如果implement UserDetails或extends User，可以判断UserDetails具体类型加上不同自定义jwt field
            if (principal instanceof SysUser) {
                SysUser sysUser = (SysUser) principal;
                additionalInfo.put("userId", sysUser.getUserId());

            }else {
                MemberUser memberUser = (MemberUser) principal;
                additionalInfo.put("userId", memberUser.getUserId());
            }
            ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
            return accessToken;
        };
    }

}
