package com.jwolf.service.msg.mq;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

//@Component
@Slf4j
@EnableScheduling
public class NonAutoconfigurationRocketMQTest {

    private String NAMESVR__ADDR = "192.168.154.140:9876";

    @Scheduled(fixedRate = 10000L)
    public void send() throws Exception {

// 1 创建消息生产者，指定生成组名
        DefaultMQProducer defaultMQProducer = new DefaultMQProducer("test-producer-group");
// 2 指定NameServer的地址
        defaultMQProducer.setNamesrvAddr(NAMESVR__ADDR);
// 3 启动生产者
        defaultMQProducer.start();
// 4 构建消息对象，主要是设置消息的主题、标签、内容
        Message message = new Message("test-topic", "test-tag", "test-key", ("测试消息发送").getBytes());
// 5 发送消息
        SendResult result = defaultMQProducer.send(message);
        System.out.println("SendResult-->" + result);
// 6 关闭生产者
        defaultMQProducer.shutdown();


// 1 创建消费者，指定所属的消费者组名
        DefaultMQPushConsumer defaultMQPushConsumer = new DefaultMQPushConsumer("test-consumer-group");
// 2 指定NameServer的地址
        defaultMQPushConsumer.setNamesrvAddr(NAMESVR__ADDR);
// 3 指定消费者订阅的主题和标签
        defaultMQPushConsumer.subscribe("test-topic", "*");
// 4 进行订阅：注册回调函数，编写处理消息的逻辑
        defaultMQPushConsumer.registerMessageListener((List<MessageExt> list, ConsumeConcurrentlyContext context) -> {
            //并且返回ConsumeConcurrentlyStatus.RECONSUME_LATER
            System.out.println("消费者回调"+list.toString());
            context.getDelayLevelWhenNextConsume();
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });

// 5 启动消费者
        defaultMQPushConsumer.start();
    }


}