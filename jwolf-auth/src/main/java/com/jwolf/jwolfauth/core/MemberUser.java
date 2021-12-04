package com.jwolf.jwolfauth.core;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * Description: TODO
 *
 * @author majun
 * @version 1.0
 * @date 2021-12-05 0:00
 */
@Getter
public class MemberUser extends User{
    private Long userId;

    public MemberUser(Long userId,String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.userId=userId;
    }
}
