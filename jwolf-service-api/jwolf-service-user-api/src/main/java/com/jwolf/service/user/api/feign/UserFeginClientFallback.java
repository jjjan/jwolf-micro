package com.jwolf.service.user.api.feign;

import org.springframework.stereotype.Component;

@Component
public class  UserFeginClientFallback implements UserFeginClient {

    @Override
    public String getUserByFeign(long id) {
        return "fallback";
    }

    @Override
    public boolean updateUser(long id) {
        return false;
    }
}