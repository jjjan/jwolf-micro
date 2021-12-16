package com.jwolf.jwolfauth.config;

import cn.hutool.core.codec.Base64;
import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSONUtil;
import com.google.common.collect.Lists;
import com.jwolf.common.bean.ResultEntity;
import com.jwolf.jwolfauth.constant.AuthConstant;
import com.jwolf.jwolfauth.core.MemberUser;
import com.jwolf.jwolfauth.core.MemberUserDetailsServiceImpl;
import com.jwolf.jwolfauth.core.SysUser;
import com.jwolf.jwolfauth.core.SysUserDetailsServiceImpl;
import com.jwolf.jwolfauth.extension.captcha.CaptchaTokenGranter;
import com.jwolf.jwolfauth.extension.mobile.SmsCodeTokenGranter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.security.KeyPair;
import java.util.*;

@Configuration
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private RedisTemplate redisTemplate;


    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                //client1为jwolf-manage sso授权登录的配置,
                .withClient(AuthConstant.JWOLF_MANAGE_CLIENTID)
                .secret(passwordEncoder().encode("secret1"))
                .authorizedGrantTypes("authorization_code", "refresh_token", "captcha")
                .scopes("all")
                .accessTokenValiditySeconds(10)
                .refreshTokenValiditySeconds(10)
                .redirectUris("http://localhost:8888/jwolf/manage/login", "http://192.168.154.143:8888/jwolf/manage/login", "http://192.168.154.143/jwolf/manage/login") // 授权成功后运行跳转的url，sso客户端默认/login，可在client端通过security.oauth2.sso.login-path修改为其它
                .autoApprove(true)  // true则自动授权,跳过授权页面点击步骤
                .and()
                //第三方授权登录时可以再这里追加，如果要做到类似微信授权登录一样，就需要从DB读取client信息。
                //并开放注册页面让用户注册，appId=clientId,appSecret=secret，并要求用户提供回调地址，这正是微信接入申请能拿到的三个核心参数
                //从DB查询client信息，implement ClientDetailsService即可，略

                //client2 暂划给jwolf-member用户,请求/auth/token时携带自定义字段userType=memberuser,CustomDaoAuthenticationProvider根据该字段查询对应userDetail
                //http://localhost:9402/oauth/token?grant_type=password&client_secret=secret2&client_id=client2&username=memberuser1&password=123456&userType=memberuser
                .withClient("client2")
                .secret(passwordEncoder().encode("secret2"))
                .authorizedGrantTypes("refresh_token", "password","sms_code")
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

        // 获取原有默认授权模式(授权码模式、密码模式、客户端模式、简化模式)的授权者
        List<TokenGranter> granterList = new ArrayList<>(Arrays.asList(endpoints.getTokenGranter()));

        // 添加手机短信验证码授权模式的授权者
        granterList.add(new SmsCodeTokenGranter(endpoints.getTokenServices(), endpoints.getClientDetailsService(),
                endpoints.getOAuth2RequestFactory(), authenticationManager
        ));
        // 添加验证码授权模式授权者
        granterList.add(new CaptchaTokenGranter(endpoints.getTokenServices(), endpoints.getClientDetailsService(),
                endpoints.getOAuth2RequestFactory(), authenticationManager, redisTemplate
        ));

        CompositeTokenGranter compositeTokenGranter = new CompositeTokenGranter(granterList);


        endpoints
                .accessTokenConverter(jwtAccessTokenConverter())
                .authenticationManager(authenticationManager)
                .tokenGranter(compositeTokenGranter)
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
        return tokenServices;

    }


    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        security
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("permitAll()")
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
        //读取classpath,或绝对路径，或bytearray的keyStore
        //String encode = Base64.encode(new File("c:/jwolf.jks"));
        byte[] keyStore = Base64.decode(AuthConstant.BASE64_JWT_KEYSTORE);
        KeyStoreKeyFactory factory = new KeyStoreKeyFactory(new ByteArrayResource(keyStore), "123456".toCharArray());
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

            } else {
                MemberUser memberUser = (MemberUser) principal;
                additionalInfo.put("userId", memberUser.getUserId());
            }
            ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
            return accessToken;
        };
    }

    /**
     * 自定义认证异常响应数据
     */
    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, e) -> {
            response.setStatus(HttpStatus.HTTP_OK);
            response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Cache-Control", "no-cache");
            ResultEntity result = ResultEntity.fail(e.getMessage());
            response.getWriter().print(JSONUtil.toJsonStr(result));
            response.getWriter().flush();
        };
    }
}
