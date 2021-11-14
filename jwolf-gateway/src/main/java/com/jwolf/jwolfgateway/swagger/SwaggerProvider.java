package com.jwolf.jwolfgateway.swagger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
  swagger资源处理
 */
@Component
@Primary
public class SwaggerProvider implements SwaggerResourcesProvider {
    //固定写法
    public static final String CUSTOM_SUFFIX = "/v2/api-docs";
    @Autowired
    private RouteLocator routeLocator;
    @Value("${spring.application.name}")
    private String applicationName;


    @Override
    public List<SwaggerResource> get() {
        List<String> routeHosts = new ArrayList<>();
        // 获取所有可用的host：serviceId
        routeLocator.getRoutes()
                .filter(route -> route.getUri().getHost() != null)
                .filter(route -> !applicationName.equals(route.getUri().getHost()))
                .subscribe(route -> routeHosts.add(route.getUri().getHost()));

        return routeHosts.stream().distinct().map(appName -> {
                    String url = "/" + appName.toLowerCase() + CUSTOM_SUFFIX;
                    SwaggerResource swaggerResource = new SwaggerResource();
                    swaggerResource.setUrl(url);
                    swaggerResource.setName(appName);
                    return swaggerResource;
                }).collect(Collectors.toList());
    }
}