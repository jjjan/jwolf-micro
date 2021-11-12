package com.jwolf.service.user.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.LocalDateTime;

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
@Slf4j
public class MybatisPlusConfig {
    //性能分析拦截器，不建议生产使用 用来观察 SQL 执行情况及执行时长
    @Bean
    @Profile({"dev", "test"})
    public PerformanceInterceptor performanceInterceptor() {
        return new PerformanceInterceptor().setFormat(true);
    }

    //分页插件
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }


    //自动填充
    @Bean
    public MetaObjectHandler setMetaObjectHandler() {
        return new MetaObjectHandler() {
            @Override
            public void insertFill(MetaObject metaObject) {
                log.info("auto-fill:createTime");
                setFieldValByName("createTime", LocalDateTime.now(), metaObject);
            }

            @Override
            public void updateFill(MetaObject metaObject) {
                log.info("auto-update:updateTime");
                setFieldValByName("updateTime", LocalDateTime.now(), metaObject);

            }
        };
    }

    //乐观锁 ,字段version,根据ID修改时如果传入了version字段，set version=version+1 where version=xx
    @Bean
    public OptimisticLockerInterceptor optimisticLockerInterceptor() {
        return new OptimisticLockerInterceptor();
    }
    //逻辑删除插件不用显示配置
    //根据id删除，包括批量删除会转换为update deleleted=1@Bean
}
