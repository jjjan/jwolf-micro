package com.jwolf.service.user.listener;

import com.jwolf.common.constant.RedisConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * 取消订单,超期默认好评等业务,考试到期自动交卷结束等
 */

@Configuration
@Slf4j
public class RedisKeyExpireListenerConfig {

    @Value("${spring.redis.database:0}")
    private String redisDatabase = "0";


    //notify-keyspace-events Ex
    @Bean
    public RedisMessageListenerContainer container(RedisConnectionFactory factory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(factory);
        //container.setTopicSerializer(new StringRedisSerializer());
        container.addMessageListener((message, pattern) -> handleKeyExpire(message), new PatternTopic("__keyevent@" + redisDatabase + "__:expired")); //监听16库的key失效
        return container;
    }


    private void handleKeyExpire(Message message) {
        String redisKey = new String(message.getBody());
        //监听到有考试结束,修改考试状态及结束时间
        if (redisKey.startsWith(RedisConstant.CHAT_ROOM_USER)) {
            String examId = redisKey.replace(RedisConstant.CHAT_ROOM_USER, "");
            log.warn("考试超时自动提交试卷ID:{},结果:{}", examId, 0);
        }
    }

}