package com.jwolf.service.user.api.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
//指定url直接调用 不走注册中心
//@FeignClient(value = "user",url = "http://localhost:8881",fallback = UserFeginClientFallback.class)
@FeignClient(value = "user"   ,fallback = UserFeginClientFallback.class)
public interface UserFeginClient {
    @GetMapping(value = "/user/user/feign")
    String getUserByFeign( @RequestParam("id") long id);


}