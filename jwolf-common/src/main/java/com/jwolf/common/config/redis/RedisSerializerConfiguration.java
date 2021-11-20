package com.jwolf.common.config.redis;

import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
  *
  * @author: majun
  * @description: redis 序列化
  */
 @Configuration
 @Slf4j
 public class RedisSerializerConfiguration {

   @Bean
   @ConditionalOnMissingBean(RedisTemplate.class)
   public RedisTemplate<String, Object> redisTemplate(
       RedisConnectionFactory redisConnectionFactory) {
       //jackson需要手动实现serializer
     /* Jackson2JsonRedisSerializer<Object> serializer =
         new Jackson2JsonRedisSerializer<>(Object.class);
     ObjectMapper objectMapper =
         new ObjectMapper()
             .registerModule(new ParameterNamesModule())
             .registerModule(new Jdk8Module())
             .registerModule(new JavaTimeModule());
     objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
     objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
     serializer.setObjectMapper(objectMapper);*/

     //StringRedisSerializer存入或取出都得手动序列化和反序列化
     //StringRedisSerializer serializer = new StringRedisSerializer(Charset.forName("UTF-8"));
     FastJsonRedisSerializer<Object> serializer = new FastJsonRedisSerializer<>(Object.class);
     RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
     redisTemplate.setConnectionFactory(redisConnectionFactory);
     redisTemplate.setKeySerializer(new StringRedisSerializer());
     redisTemplate.setValueSerializer(serializer);
     redisTemplate.setHashKeySerializer(new StringRedisSerializer());
     redisTemplate.setHashValueSerializer(serializer);
     redisTemplate.afterPropertiesSet();
     return redisTemplate;
   }



 }
