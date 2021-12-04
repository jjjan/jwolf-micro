package com.jwolf.jwolfmanage.util;

import cn.hutool.json.JSONObject;
import cn.hutool.jwt.JWT;
import com.jwolf.jwolfmanage.bean.vo.UserVO;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;

import java.util.List;

/**
 * Description: TODO
 *
 * @author majun
 * @version 1.0
 * @date 2021-12-05 0:13
 */
public class TokenUtil {

    public static UserVO getCurrentSysUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) authentication.getDetails();
        JSONObject claimsJson = JWT.create()
                .parse(details.getTokenValue())
                .getPayload()
                .getClaimsJson();
        return new UserVO()
                .setRoles((List<String>) claimsJson.get("authorities"))
                .setUsername(claimsJson.getStr("user_name"))
                .setUserId(Long.valueOf(claimsJson.getStr("userId")))
                .setJti(claimsJson.getStr("jti"));

    }

}
