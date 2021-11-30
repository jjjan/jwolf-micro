package com.jwolf.jwolfauth.config;

import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //自定义本地登录页面及成功回调path
        http.formLogin()
                .loginPage("/jwolfLogin") //登录页view,默认/login
                .loginProcessingUrl("/authentication/form")//与登录页form提交的url一致,
                .successForwardUrl("/oauth/to-app")
                .defaultSuccessUrl("/oauth/to-app", false)
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
}