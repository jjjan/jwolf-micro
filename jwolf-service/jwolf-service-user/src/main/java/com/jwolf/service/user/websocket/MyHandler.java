package com.jwolf.service.user.websocket;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class MyHandler extends TextWebSocketHandler {


    private static final Map<Long, WebSocketSession> sessions = new ConcurrentHashMap<>();


    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message)
            throws IOException {
        System.out.println("获取到消息 >> " + message.getPayload());
        if (session != null && session.isOpen()) { //有session且open状态
            session.sendMessage(new TextMessage("消息已收到"));
        }


    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws
            Exception {
        String uid = (String) session.getAttributes().get("uid");
        session.sendMessage(new TextMessage(uid + ", 你好！欢迎连接到ws服务"));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        System.out.println("断开连接！");
    }
}
