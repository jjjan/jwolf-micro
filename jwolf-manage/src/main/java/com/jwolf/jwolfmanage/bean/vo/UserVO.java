package com.jwolf.jwolfmanage.bean.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * Description: TODO
 *
 * @author majun
 * @version 1.0
 * @date 2021-12-04 22:55
 */
@Getter
@Setter
@Accessors(chain = true)
public class UserVO {
    private Long userId;

    private String username;
    //jwt唯一标识
    private String jti;
    //角色
    private List<String> roles;
    //权限
    private List<String> perms ;
}
