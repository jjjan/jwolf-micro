package com.jwolf.jwolfauth.controller;

import cn.hutool.core.map.MapBuilder;
import cn.hutool.jwt.JWTUtil;
import com.jwolf.jwolfauth.constant.AuthConstant;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;
import java.time.LocalDateTime;
import java.util.Map;

@Controller
@SessionAttributes("authorizationRequest")
public class CustomAuthController {
    @Autowired
    private KeyPair keyPair;

    /**
     * 去本地表单登录页，@{link {@link com.jwolf.jwolfauth.config.SecurityConfig}}
     *
     * @return
     */
    @RequestMapping("/jwolfLogin")
    public String gotoMyLogin() {
        return "jwolfLogin";
    }

    /**
     * sso client登录走该页面,默认端点是/login，jwolf-auth配置文件指定了该端点
     *
     * @return
     */
    @RequestMapping("/jwolfSSOLogin")
    public String gotoJwolfSSOLogin() {
        return "jwolfSSOLogin";
    }

    /**
     * gateway 集成Oauth2后 jwt解密需要从授权服务器获取公钥
     *
     * @return
     */
    @GetMapping("/rsa/publicey")
    public Map<String, Object> getPublicKey() {
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAKey key = new RSAKey.Builder(publicKey).build();
        return new JWKSet(key).toJSONObject();
    }

    /**
     * 内置端点，如果第三方（如jwolf-manage）以授权码方式接入，用户登陆后输入账号密码后进入该端点
     * 点击授权后跳回原页面
     *
     * @param model
     * @param request
     * @return
     */
    @RequestMapping("/oauth/confirm_access")
    public ModelAndView getAccessConfirmation(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) {
        AuthorizationRequest authorizationRequest = (AuthorizationRequest) model.get("authorizationRequest");
        ModelAndView mv = new ModelAndView();
        mv.setViewName("mygrant");
        mv.addObject("clientId", authorizationRequest.getClientId());
        mv.addObject("scopes", authorizationRequest.getScope());
        return mv;

    }

    /**
     * 本地表单登录后走该path（sso授权登录是走/oauth/confirm_access到授权页面，同意后重定向到redirect_url）
     *
     * @param model
     * @param response
     * @param request
     * @throws IOException
     */
    @RequestMapping("/oauth/to-app")
    public void toApp(Map<String, Object> model, HttpServletResponse response, HttpServletRequest request) throws IOException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Map<String, Object> map = MapBuilder.<String, Object>create()
                .put("uid", user.getUsername())
                .put("expire_time", LocalDateTime.now().plusDays(AuthConstant.TOKEN__EXPIRE))
                .build();
        String token = JWTUtil.createToken(map, "1234".getBytes());
        response.sendRedirect(AuthConstant.JWOLF_FRONTEND_INDEX + "?token=" + token);
    }
}