package com.jwolf.jwolfauth.controller;

import lombok.SneakyThrows;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.request.AuthGiteeRequest;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Description: TODO
 *
 * @author majun
 * @version 1.0
 * @date 2021-11-21 14:31
 */
@RestController
@RequestMapping("/oauth")
public class SocialOauth2Controller {

    @RequestMapping("/render")
    public void renderAuth(HttpServletResponse response) throws IOException {
        AuthRequest authRequest = getAuthRequest();
        response.sendRedirect(authRequest.authorize(AuthStateUtils.createState()));
    }

    @SneakyThrows
    @RequestMapping("/callback")
    public Object login(AuthCallback callback, HttpServletResponse response) {
        AuthRequest authRequest = getAuthRequest();
        AuthResponse res = authRequest.login(callback);
        //从response拿到第三方用户id,token及refresh_token，根据id可到自己DB查询该第三方id绑定的本地用户
        //后续处理待gateway整合oauth2实现
        return res;

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





