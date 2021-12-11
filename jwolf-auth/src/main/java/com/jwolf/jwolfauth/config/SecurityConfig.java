package com.jwolf.jwolfauth.config;

import com.jwolf.jwolfauth.core.CustomDaoAuthenticationProvider;
import com.jwolf.jwolfauth.core.MemberUserDetailsServiceImpl;
import com.jwolf.jwolfauth.core.SysUserDetailsServiceImpl;
import com.jwolf.jwolfauth.extension.mobile.SmsCodeAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private SysUserDetailsServiceImpl sysUserDetailsService;
    @Autowired
    private MemberUserDetailsServiceImpl memberUserDetailsService;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // <<<会员用户,系统用户同时支持表单登录会有混到一起，暂定系统用户走验证码用户密码登录，会员用户走手机验证码及社交登录>>>
                // <<<一般较大企业如微信支付宝需要提供表单登录，以便中小企业社交授权登录,中小企业自己如果多个内部系统可考虑sso表单登录>>>
                // <<<系统用户与会员用户也可以同时支持表单登录,需要重写默认的DaoAuthenticationProvider>>>
                .formLogin().loginPage("/jwolfSSOLogin") //登录页view,默认/login
                .loginProcessingUrl("/authentication/form")//与登录页form提交的url一致,
                .defaultSuccessUrl("/oauth/to-app", false)//如果是重定向到登录页则走回原路径，如果直接走登录页则走to-app,该path指定登录后默认重定向地址
                .and()
                //不需要保护的uri
                .authorizeRequests().antMatchers("/oauth/**", "/jwolfLogin", "/jwolfSSOLogin", "/rsa/publicKey", "/authentication/form", "/sms-code", "/captcha-code").permitAll()
                .requestMatchers(EndpointRequest.toAnyEndpoint()).permitAll()
                .anyRequest().authenticated()
                .and()
                .logout()
                .logoutUrl("/oauth/logout") //登出url,该url不用显示定义
                .logoutSuccessUrl("http://baidu.com")
                .invalidateHttpSession(true)
                .deleteCookies("OAUTH2-CLIENT1-SESSIONID","JSESSIONID")
                .clearAuthentication(true)
                .permitAll()
                .and()
                .csrf().disable();

    }


    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    @Override
    public void configure(AuthenticationManagerBuilder auth) {
        auth
                // <<<会员用户,系统用户同时支持表单登录会有混到一起，暂定系统用户走验证码用户密码登录，会员用户走手机验证码及社交登录>>>
                // <<<一般较大企业如微信支付宝需要提供表单登录，以便中小企业社交授权登录,中小企业自己如果多个内部系统可考虑sso表单登录>>>
                // <<<系统用户与会员用户也可以同时支持表单登录,需要重写默认的DaoAuthenticationProvider>>>
                //.authenticationProvider(sysUserDaoAuthenticationProvider())
                .authenticationProvider(customDaoAuthenticationProvider())
                .authenticationProvider(smsCodeAuthenticationProvider());
    }

    /**
     * 会员用户认证提供者-会员用户登录走memberUserDetailsService，即查询member_user表的用户进行登录认证
     *
     * @return
     */
    @Bean
    public CustomDaoAuthenticationProvider customDaoAuthenticationProvider() {
        //DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        CustomDaoAuthenticationProvider provider = new CustomDaoAuthenticationProvider();
        provider.setUserDetailsService(memberUserDetailsService,sysUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        provider.setHideUserNotFoundExceptions(false);
        return provider;
    }

    /**
     * 手机验证码认证授权提供者
     *
     * @return
     */
    @Bean
    public SmsCodeAuthenticationProvider smsCodeAuthenticationProvider() {
        SmsCodeAuthenticationProvider provider = new SmsCodeAuthenticationProvider();
        provider.setUserDetailsService(memberUserDetailsService);
        provider.setRedisTemplate(redisTemplate);
        return provider;
    }
//
//    /**
//     * 系统用户认证提供者-系统用户登录走sysUserDetailsService，即查询sys_user表的用户进行登录认证
//     *
//     * @return
//     */
//    @Bean
//    public DaoAuthenticationProvider sysUserDaoAuthenticationProvider() {
//        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
//        provider.setUserDetailsService(sysUserDetailsService);
//        provider.setPasswordEncoder(passwordEncoder);
//        provider.setHideUserNotFoundExceptions(false);
//        return provider;
//    }

}