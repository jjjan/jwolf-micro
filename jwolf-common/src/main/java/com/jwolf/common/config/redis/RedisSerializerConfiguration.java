package com.jwolf.common.config.redis;

import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jwolf.common.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author: majun
 * @description: redis 序列化
 */
@Configuration
@Slf4j
public class RedisSerializerConfiguration {

    //@Bean(name = "redisTemplate")//必须加name=redisTemplate 或方法名用redisTemplate，否则注入时多个RedisTemplate类型异常或使用redisTemplate自定义序列化无效,容器中存在两个redisTemplate？
    public RedisTemplate<String, Object> fastjsonRedisTemplate(RedisConnectionFactory factory) {
        FastJsonRedisSerializer<Object> serializer = new FastJsonRedisSerializer<>(Object.class);
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(serializer);
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(serializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    /**
     * 如果使用jackson序列化与反序列化，建议与她保持一致，避免采坑@{link {@link com.jwolf.common.config.webmvc.JacksonSerializerConfiguration}}
     *
     * @param factory
     * @return
     */
    @Bean(name = "redisTemplate")//必须加name=redisTemplate 或方法名用redisTemplate，否则注入时多个RedisTemplate类型异常或使用redisTemplate自定义序列化无效,容器中存在两个redisTemplate？
    public RedisTemplate<String, Object> jacksonRedisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringRedisSerializer); // key
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonUtils.configObjectMapper(objectMapper);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }


}
