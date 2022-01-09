package com.jwolf.service.user.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.jwolf.common.bean.ResultEntity;
import com.jwolf.service.user.api.entity.User;
import com.jwolf.service.user.config.SentinelHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.Response;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Description: 测试类
 *
 * @author majun
 * @version 1.0
 * @date 2022-01-09 16:33
 */
@RestController
public class TestController {

    @Autowired
    private DiscoveryClient discoveryClient;
    @Autowired
    private ApplicationContext context;
    @Autowired
    private LoadBalancerClientFactory balancerClientFactory;


    /**
     * 研究一下负载均衡
     * @param id
     * @return
     */
    @Operation(summary = "test-loadbalancer")
    @GetMapping("/test")
    public ResultEntity<User> test(@Parameter(description = "用户id") Long id) {
        // discoveryClient获取服务列表，根据xx负载均衡算法获取特定实例（略）
        List<ServiceInstance> instanceList = discoveryClient.getInstances("user");
        // 或内置的balancerClientFactory选取某个服务
        Publisher<Response<ServiceInstance>> user = balancerClientFactory.getInstance("user").choose();
        ServiceInstance serviceInstance = Mono.from(user).block().getServer();
        // 调用微服务
        return new RestTemplate().getForObject(serviceInstance.getUri() + "/user/user/detail", ResultEntity.class);
    }

}
