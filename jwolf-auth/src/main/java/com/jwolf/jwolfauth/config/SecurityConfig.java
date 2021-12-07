package com.jwolf.jwolfauth.config;

import com.jwolf.jwolfauth.core.MemberUserDetailsServiceImpl;
import com.jwolf.jwolfauth.core.SysUserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
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

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //自定义本地登录页面及成功回调path
        http.formLogin()
                .loginPage("/jwolfLogin") //登录页view,默认/login
                .loginProcessingUrl("/authentication/form")//与登录页form提交的url一致,
                .successForwardUrl("/oauth/to-app")
                .defaultSuccessUrl("/oauth/to-app", false)//如果是重定向到登录页则走回原路径，如果直接走登录页则走to-app
                .and()
                //SSO授权登录走该页面，不要定义successForwardUrl，因为SSO授权后要回调redirect_url
                .formLogin().loginPage("/jwolfSSOLogin") //登录页view,默认/login
                .loginProcessingUrl("/authentication/form")//与登录页form提交的url一致,
                .and()
                //不需要保护的uri
                .authorizeRequests().antMatchers("/oauth/**", "/jwolfLogin", "/jwolfSSOLogin", "/rsa/publicKey", "/authentication/form").permitAll()
                .requestMatchers(EndpointRequest.toAnyEndpoint()).permitAll()
                .anyRequest().authenticated()
                .and()
                .logout()
                .logoutUrl("/logout") //默认logout
                //.logoutSuccessUrl("/xxxx")
                .deleteCookies("OAUTH2-CLIENT1-SESSIONID")
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
        auth.authenticationProvider(memeberUserDaoAuthenticationProvider())
                .authenticationProvider(sysUserDaoAuthenticationProvider());
    }

    /**
     * 会员用户认证提供者-会员用户登录走memberUserDetailsService，即查询member_user表的用户进行登录认证
     *
     * @return
     */
    @Bean
    public DaoAuthenticationProvider memeberUserDaoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(memberUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        provider.setHideUserNotFoundExceptions(false);
        return provider;
    }

    /**
     * 系统用户认证提供者-系统用户登录走sysUserDetailsService，即查询sys_user表的用户进行登录认证
     *
     * @return
     */
    @Bean
    public DaoAuthenticationProvider sysUserDaoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(sysUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        provider.setHideUserNotFoundExceptions(false);
        return provider;
    }

}