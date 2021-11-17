package com.jwolf.service.msg.constant;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Description: TODO
 *
 * @author majun
 * @version 1.0
 * @date 2021-11-17 22:31
 */
@Getter
@AllArgsConstructor
public enum  MsgTypeEnum {
    Login(1,"登录"),
    Logout(2,"登出");
    private int key;
    private String description;
}
