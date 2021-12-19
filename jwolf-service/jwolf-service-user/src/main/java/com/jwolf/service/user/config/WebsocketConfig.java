package com.jwolf.service.user.config;

import com.jwolf.service.user.websocket.MyHandler;
import com.jwolf.service.user.websocket.MyHandshakeInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    @Autowired
    private MyHandler myHandler;
    @Autowired
    private MyHandshakeInterceptor myHandshakeInterceptor;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(this.myHandler, "/ws/jwolf") //可以使用http://localhost:8881/index.html 或第三方网站测试http://coolaf.com/tool/chattest,不要配置【withSockJS()】
                .setAllowedOriginPatterns("*")
                .addInterceptors(this.myHandshakeInterceptor);//添加拦截器
        //.withSockJS();  //不要配置，SockJS版本websockt操作才需要
    }
}
