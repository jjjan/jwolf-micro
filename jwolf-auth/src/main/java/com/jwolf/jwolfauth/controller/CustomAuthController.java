package com.jwolf.jwolfauth.controller;

import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@SessionAttributes("authorizationRequest")
public class CustomAuthController {
    /**
     * 跳到自定义登录页面，需要在security配置该path
     *
     * @return
     */
    @RequestMapping("/mylogin")
    public String getMyLogin() {
        return "mylogin";
    }

    /**
     * 跳到自定义授权页面（oauth2默认使用该path）
     *
     * @param model
     * @param request
     * @return
     * @throws Exception
     */

    @RequestMapping("/oauth/confirm_access")
    public ModelAndView getAccessConfirmation(Map<String, Object> model, HttpServletRequest request) throws Exception {
        AuthorizationRequest authorizationRequest = (AuthorizationRequest) model.get("authorizationRequest");
        ModelAndView mv = new ModelAndView();
        mv.setViewName("mygrant");
        mv.addObject("clientId", authorizationRequest.getClientId());
        mv.addObject("scopes", authorizationRequest.getScope());
        return mv;

    }
}