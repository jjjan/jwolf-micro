package com.jwolf.jwolfauth.controller;

import cn.hutool.core.map.MapBuilder;
import cn.hutool.jwt.JWTUtil;
import com.jwolf.jwolfauth.constant.AuthConstant;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthGiteeRequest;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Description: TODO
 *
 * @author majun
 * @version 1.0
 * @date 2021-11-21 14:31
 */
@RestController
@RequestMapping("/oauth")
@Slf4j
public class SocialOauth2Controller {

    @RequestMapping("/render")
    public void renderAuth(HttpServletResponse response) throws IOException {
        AuthRequest authRequest = getAuthRequest();
        response.sendRedirect(authRequest.authorize(AuthStateUtils.createState()));
    }

    /**
     * 第三方Gitee回调时从根据code->accessToken->userInfo->uuid->查自己的用户表->token->前端首页
     *
     * @param callback
     * @param response
     */
    @SneakyThrows
    @RequestMapping("/callback")
    public void login(AuthCallback callback, HttpServletResponse response) {
        //获取令牌
//        String body="grant_type=authorization_code&code="+code+
//                "&client_id="+"d3f14becdca28bc9fe4fd674b4791cbca803d5c2958d3dbf0bc04ab69dae1880"+
//                "&redirect_uri="+"http://127.0.0.1:9402/oauth/callback"+
//                "&client_secret="+"3fbc551fdec14ad0584ea473d4511e857ab2f6351e5356d57bac1109c7639452";
//        String tokenInfo = HttpRequest.post("https://gitee.com/oauth/token?"+body)
//                .contentType("application/x-www-form-urlencoded")
//                .charset("utf-8")
//                .execute()
//                .body();
//        String userInfo = HttpRequest.get("https://gitee.com/api/v5/user?access_token=TokenInfoxxxx").execute().body();
        //框架封装了以上两个步骤!!!
        AuthRequest authRequest = getAuthRequest();
        AuthResponse res = authRequest.login(callback);
        Assert.isTrue(res.ok(), "gitee登录失败");
        AuthUser data = (AuthUser) res.getData();
        String uuid = data.getUuid();
        //从response拿到第三方用户id，根据id可到自己DB查询该第三方id绑定的本地用户,无则绑定有则跳转
        String userId = uuid + "getFromDbByGiteeUuid";
        Map<String, Object> map = MapBuilder.<String, Object>create()
                .put("uid", userId)
                .put("expire_time", LocalDateTime.now().plusDays(AuthConstant.TOKEN__EXPIRE))
                .build();
        String token = JWTUtil.createToken(map, "1234".getBytes());
        //携带token跳到jwolf.com的首页，jwolf前端从URL路由拿到token存入sessionStorage,前端全局请求拦截器从sessionStorage拿到token放请求头
        response.sendRedirect(AuthConstant.JWOLF_FRONTEND_INDEX + "?token=" + token);

    }


    /**
     * https://gitee.com/oauth/applications/  授权设置的三个参数，回调地址为第三方登录后点击授权后请求的我方接口（浏览器请求该接口）
     *
     * @return
     */
    private AuthRequest getAuthRequest() {
        return new AuthGiteeRequest(AuthConfig.builder()
                .clientId("d3f14becdca28bc9fe4fd674b4791cbca803d5c2958d3dbf0bc04ab69dae1880")
                .clientSecret("3fbc551fdec14ad0584ea473d4511e857ab2f6351e5356d57bac1109c7639452")
                .redirectUri("http://127.0.0.1:9402/oauth/callback")
                .build());
    }
}





