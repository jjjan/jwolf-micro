package com.jwolf.service.user.websocket;

import com.jwolf.common.exception.CommonException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Slf4j
public class MyHandler extends TextWebSocketHandler {

    //用来存放每个客户端对应的WebSocket session会话对象
    private static Map<String, WebSocketSession> webSocketMap = new ConcurrentHashMap<>();

    /**
     * 单发
     *
     * @param userId
     * @param msg
     */
    @SneakyThrows
    public static void send2User(String userId, String msg) {
        webSocketMap.entrySet().stream()
                .filter(entry -> entry.getKey().equals(userId))
                .findFirst().orElseThrow(() -> new CommonException("用户已下线" + userId))
                .getValue()
                .sendMessage(new TextMessage(msg));
    }

    /**
     * 并行群发
     *
     * @param msg
     */
    public static void sendAllUser(String msg) {
        AtomicInteger errCount = new AtomicInteger(0);
        webSocketMap.entrySet().parallelStream().forEach(entry -> {
            try {
                entry.getValue().sendMessage(new TextMessage(msg));
            } catch (IOException e) {
                int i = errCount.incrementAndGet();
                log.error("并行群发出现错误个数{}", i, e);
            }
        });
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message)
            throws IOException {
        log.info("获取到消息 >> " + message.getPayload());
        if (session != null && session.isOpen()) { //有session且open状态
            session.sendMessage(new TextMessage("消息已收到"));
        }


    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.error("websocket error>>>>>", exception);
        super.handleTransportError(session, exception);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String userId = (String) session.getAttributes().get("userId");
        webSocketMap.put(userId, session);
        session.sendMessage(new TextMessage(userId + ", 你好！欢迎连接到ws服务,当前人数" + webSocketMap.size()));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        String userId = (String) session.getAttributes().get("userId");
        boolean anyMatch = webSocketMap.entrySet().stream().anyMatch(entry -> entry.getKey().equals(userId));
        if (anyMatch) {
            webSocketMap.remove(userId);
            log.info("成功断开连接，剩余人数{}", webSocketMap.size());
        } else {
            log.warn("未找到{}用户！", userId);
        }
    }

}
