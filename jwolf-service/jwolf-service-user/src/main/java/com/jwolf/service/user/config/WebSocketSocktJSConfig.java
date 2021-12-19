package com.jwolf.service.user.config;

import com.jwolf.service.user.websocket.MyHandshakeInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

//@Configuration
//@EnableWebSocketMessageBroker
public class WebSocketSocktJSConfig implements WebSocketMessageBrokerConfigurer {
    //@Autowired
    //private MyHandler myHandler;//SocktJS版本不需要，是根据不同event类型来判断处理的
    @Autowired
    private MyHandshakeInterceptor myHandshakeInterceptor;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        //默认"",如后端接口为@MessageMapping("/xxx")，前端stomJS发送要加前缀：this.stompClient.send("/app/send", {}, JSON.stringify(this.message))},
        //registry.setApplicationDestinationPrefixes("/app");
        registry.enableSimpleBroker("/topic");
        //默认"",有前缀前端订阅topic需要加前缀,this.stompClient.subscribe('/user/topic/AAA', (msg) => {});
        //registry.setUserDestinationPrefix("/user");
    }


    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws/jwolf")
                .addInterceptors(myHandshakeInterceptor) //SockJS不配置handler,而是通过event监听实现，分为SessionConnectEvent，SessionUnsubscribeEvent，SessionSubscribeEvent，SessionDisconnectEvent等，根据不同事件做不同处理，消息发送也可以单发，群发，只是稍有封装，可以通过不同/topic区分不同消息，简单的对消息做了分类，其实自己定义一个消息类型的key也是很容易做到的
                .setAllowedOriginPatterns("*") //与非SockJS版本设置跨域为setAllowedOrigins
                .withSockJS();
    }
}