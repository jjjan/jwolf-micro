package com.jwolf.service.user.feignClientImpl;

import com.jwolf.service.user.api.entity.User;
import com.jwolf.service.user.api.feign.UserFeginClient;
import com.jwolf.service.user.service.IUserService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description: TODO
 *
 * @author majun
 * @version 1.0
 * @date 2021-11-12 0:06
 */
@RestController
public class UserFeignClientImpl implements UserFeginClient {
    @Autowired
    private IUserService userService;

    @SneakyThrows
    @Override
    public String getUserByFeign(long id) {
        return "feignUser";
    }

    @Override
    public boolean updateUser(long id) {
        //int a=1/0;
        User user = new User().setId(1L).setNickName("xxxxxx");
        return userService.updateById(user);
    }
}
