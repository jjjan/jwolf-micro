package com.jwolf.service.user.config;

import com.zaxxer.hikari.HikariDataSource;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import javax.sql.DataSource;


//@Configuration 使用clickhouse，放开该注解即可
@ConfigurationProperties(prefix = "spring.datasource.click")
@Setter
public class ClickhouseDatasourseConfiguration {
    private String driverClassName;
    private String url;
    private String username;
    private String password;
    private Integer maxActive;
    private Integer minIdle;
    private Integer maxWait;

    /**
     * spring未整合clickhourse, 优先实例化后就不再初始化mysql datasourse,mybatis-plus使用的datasourse自动走clickhouse
     * {@link org.springframework.boot.autoconfigure.jdbc.DataSourceConfiguration.Hikari#dataSource}
     * @return
     */
    @Order(value = Integer.MIN_VALUE)
    @Bean
    public DataSource dataSource() {
        HikariDataSource datasource = new HikariDataSource();
        datasource.setJdbcUrl(url);
        datasource.setKeepaliveTime(maxWait);
        datasource.setDriverClassName(driverClassName);
        datasource.setMaximumPoolSize(maxActive);
        datasource.setMinimumIdle(minIdle);
        datasource.setUsername(username);
        datasource.setPassword(password);
        return datasource;
    }
}