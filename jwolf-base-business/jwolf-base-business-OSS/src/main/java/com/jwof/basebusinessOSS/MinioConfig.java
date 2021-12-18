package com.jwof.basebusinessOSS;

import io.minio.MinioClient;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;


@Component
@ConfigurationProperties(prefix = "minio")
@Setter
public class MinioConfig {

    //host
    private String endpoint;

    //TCP/IP端口号
    private int port;

    //accessKey类似于用户ID，用于唯一标识你的账户
    private String accessKey;

    //secretKey是你账户的密码
    private String secretKey;

    //如果是true，则用的是https而不是http,默认值是true
    private Boolean secure;


    @Bean
    public MinioClient getMinioClient() {
        return MinioClient.builder()
                .credentials(accessKey, secretKey)
                .endpoint(endpoint, port, secure)
                .build();
    }
}
