package com.jwolf.service.user.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Description: TODO
 *
 * @author majun
 * @version 1.0
 * @date 2022-01-02 20:02
 */
//@Configuration
public class RedissionConfiguration {


    /**
     * 主从redission
     * 主从redis分布式锁的最大缺陷：在redis master实例宕机的时候，可能导致多个客户端同时完成加锁。
     *
     * @return
     */
    @Bean
    public RedissonClient redissonSingle() {
        Config config = new Config();
        config.useMasterSlaveServers()
                .setMasterAddress("redis://192.168.1.20:6379")
                .addSlaveAddress("redis://192.168.1.20:16379")
                .setPassword("jwolf");
        RedissonClient redissonClient = Redisson.create(config);
        System.out.println("cluster key count>>>" + redissonClient.getKeys().count());
        return redissonClient;
    }


}
