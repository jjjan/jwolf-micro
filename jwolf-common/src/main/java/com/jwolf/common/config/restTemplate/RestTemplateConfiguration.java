package com.jwolf.common.config.restTemplate;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HttpContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Description: TODO
 *
 * @author majun
 * @version 1.0
 * @date 2020-03-06 17:40
 */

@Configuration
@Slf4j
public class RestTemplateConfiguration {
    @Value("${restTemplate.connectionRequestTimeout:10000}")
    private int connectionRequestTimeout; //连接不够用的等待时间
    @Value("${restTemplate.connectTimeout:3000}")
    private int connectTimeout;
    @Value("${restTemplate.readTimeout:30000}")
    private int readTimeout;
    @Value("${restTemplate.maxConnTotal:200}")
    private int maxConnTotal;
    @Value("${restTemplate.maxConnPerRoute:50}")
    private int maxConnPerRoute;


    @Bean
    RestTemplate getRestTemplate() {
        //配置超时必须同时设置连接池，否则调用方用不主动超时，只能一直等待服务方
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(connectionRequestTimeout)
                .setConnectionRequestTimeout(connectTimeout)
                .setSocketTimeout(readTimeout).build();

        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setMaxConnTotal(maxConnTotal)
                .setMaxConnPerRoute(maxConnPerRoute)
                .setRetryHandler(new DefaultHttpRequestRetryHandler(1, true, new ArrayList<>()) {
                    @Override
                    public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
                        log.info("重试请求, 执行次数: {}, 异常: {}", executionCount, exception);
                        return super.retryRequest(exception, executionCount, context);
                    }
                })
                .setDefaultRequestConfig(requestConfig)
                .build();

        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        RestTemplate restTemplate = new RestTemplate(httpRequestFactory);
        restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
        //统一使用Fegin进行微服务调用，RestTemplate仅用作第三方API调用，替代HttpClientUtil,故不必向下游微服务传递user会话信息
        //restTemplate.setInterceptors(Lists.newArrayList(new CommonRestTemplateInterceptor()));//设置用户信息请求头
        return restTemplate;
    }


}
