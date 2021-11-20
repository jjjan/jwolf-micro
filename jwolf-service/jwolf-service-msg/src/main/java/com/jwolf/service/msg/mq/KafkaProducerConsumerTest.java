package com.jwolf.service.msg.mq;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

@Component
@Slf4j
@EnableScheduling
public class KafkaProducerConsumerTest {
    @Autowired
    private KafkaTemplate<Integer, String> kafkaTemplate;


    @Scheduled(cron = "0/50 * * * * ? ")
    public void send() {
        ListenableFuture future = kafkaTemplate.send("test5", "来自kafkaTemplate发送de消息");
        future.addCallback(o -> System.out.println("发送成功"), e -> System.err.println("发送失败," + e.getCause()));
    }
    //同一个进程的两个xxx消费者不会共同消费
    @KafkaListener(topics = "test5", groupId = "xxx")
    public void getMsg1(String msg) {
        System.out.println("xxx1" + msg);
    }

    @KafkaListener(topics = "test5", groupId = "xxx")
    public void getMsg2(String msg) {
        System.out.println("xxx2" + msg);
    }

    @KafkaListener(topics = "test5", groupId = "yyy")
    public void getMsg3(String msg) {
        System.out.println("yyy" + msg);
    }

    //配置 ack模式为手动提交才正常消费
    @KafkaListener(topics = "test5", groupId = "zzz")
    public void manualImmediateTest(ConsumerRecord<String, Object> record, Consumer consumer, Acknowledgment ack) {
        log.info("zzz" + record.value());
        log.info("consumer content = {}", consumer);
        ack.acknowledge();

    }
}