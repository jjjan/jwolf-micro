package com.jwolf.service.msg.mq;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;
 
@Component
@RocketMQMessageListener(topic = "test-topic2",consumerGroup = "test-consumer-group")
@Slf4j
public class RocketMQConsumer implements RocketMQListener<String> {
    @Override
    public void onMessage(String message) {
        System.out.println("RocketMQListener <<"+message);
    }
}