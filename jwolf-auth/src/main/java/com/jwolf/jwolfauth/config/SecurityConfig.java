package com.jwolf.jwolfauth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //http.formLogin().and().authorizeRequests().anyRequest().authenticated();
        //自定义登录与授权页面
        http.formLogin()
                .loginPage("/mylogin") //登录页view,默认/login
                .loginProcessingUrl("/authentication/form")//与登录页form提交的url一致,
                .and()
                .authorizeRequests().antMatchers("/oauth/**", "/mylogin").permitAll()
                .anyRequest().authenticated()
                .and()
                .logout()
                .logoutUrl("/logout") //默认logout
                //.logoutSuccessUrl("/xxxx")
                .deleteCookies("OAUTH2-CLIENT1-SESSIONID")
                .and()
                .csrf().disable();

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }
}