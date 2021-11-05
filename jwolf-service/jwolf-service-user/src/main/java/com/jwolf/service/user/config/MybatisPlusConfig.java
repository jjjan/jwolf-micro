package com.jwolf.service.user.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * <p>
 * Description: TODO
 * </p>
 *
 * @author majun
 * @version 1.0
 * @date 2019-06-29 12:54
 */
@Configuration
public class MybatisPlusConfig {
    // 性能分析拦截器，不建议生产使用 用来观察 SQL 执行情况及执行时长, 默认dev,test 环境开启
    @Bean
    @Profile({"dev", "test"})
    public PerformanceInterceptor performanceInterceptor() {

        //启用性能分析插件, SQL是否格式化 默认false,此处开启
        return new PerformanceInterceptor().setFormat(true);
    }

    //分页插件
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }
}
