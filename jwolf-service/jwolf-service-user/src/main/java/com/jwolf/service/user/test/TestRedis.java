package com.jwolf.service.user.test;

import com.alibaba.fastjson.JSON;
import com.jwolf.service.user.api.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


/**
 * Description: TODO
 *
 * @author majun
 * @version 1.0
 * @date 2021-11-20 22:27
 */
//@Component
@EnableScheduling
public class TestRedis {
    @Autowired
    private RedisTemplate redisTemplate;

    @Scheduled(fixedRate = 3000)
    public void testRedis() {
        System.out.println(redisTemplate.keys("*"));
        String userJson = JSON.toJSONString(new User().setId(1L).setNickName("test"));
        redisTemplate.opsForValue().set("test", userJson);
        User user = JSON.parseObject((String) redisTemplate.opsForValue().get("test"),User.class);
        System.out.println(user);
        System.out.println(redisTemplate.keys("*"));

    }
}
