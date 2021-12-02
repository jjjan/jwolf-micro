package com.jwolf.common.util;

import cn.hutool.jwt.JWT;
import com.google.common.collect.Maps;
import com.jwolf.common.bean.ReqUser;
import com.jwolf.common.exception.CommonException;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

/**
 * @author jwolf
 */
public class TokenUtils {

    private static long EXPIRE_DAYS;
    private static String SECRET;

    static {
        EXPIRE_DAYS = Long.parseLong(YmlUtils.getConfig("jwt.expireDays"));
        SECRET = YmlUtils.getConfig("jwt.secret");
    }


    /**
     * JWT生成Token. JWT构成: header, payload, signature
     */
    public static String createToken(String userId, int accessType){
        Map<String, Object> map = Maps.newHashMap();
        map.put("alg", "HS256");
        map.put("typ", "JWT");
        long currentTimeMillis = System.currentTimeMillis();
        Date expireDate = new Date(currentTimeMillis + EXPIRE_DAYS * 1000 * 60 * 60 * 24);
        return JWT.create()
                .setPayload("userId", userId)
                .setPayload("accessType", accessType)
                //.withIssuedAt(new Date(currentTimeMillis))
                .setExpiresAt(expireDate)
                .setSigner("SHA256", SECRET.getBytes(StandardCharsets.UTF_8))
                .sign();
    }


    /**
     * 根据Token获取user_id
     *
     * @return user_id
     */
    public static String getUserId(String token) throws CommonException {

        return "";
    }


    /**
     * 获取用户信息
     *
     * @return ReqUser
     */

    public static ReqUser getReqUser(String token) throws CommonException {

        return new ReqUser("", "", "");

    }

}



