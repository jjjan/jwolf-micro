package com.jwolf.jwolfmanage.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.jwt.JWT;
import com.alibaba.fastjson.JSON;
import com.jwolf.common.bean.ResultEntity;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;

/**
 * Description: TODO
 *
 * @author majun
 * @version 1.0
 * @date 2021-11-29 1:05
 */
@RestController
public class TestController {


    @Operation(summary = "用户详情测试,授权服务器返回的jwt token")
    @GetMapping("/manager-index")
    public ResultEntity getDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        OAuth2AuthenticationDetails details =(OAuth2AuthenticationDetails) authentication.getDetails();
        JSONObject claimsJson = JWT.create()
                .setSigner(null, "dev".getBytes(StandardCharsets.UTF_8))
                .parse(details.getTokenValue())
                .getPayload()
                .getClaimsJson();
        System.out.println(JSON.toJSONString(claimsJson));
        return ResultEntity.success(claimsJson);
    }
    @Operation(summary = "测试1")
    @GetMapping("/manager-test1")
    @PreAuthorize("hasAnyAuthority('super')")
    public ResultEntity getTest1(int id) {
        return ResultEntity.success(id);
    }
}
