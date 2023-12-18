package rednosed.app.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import rednosed.app.exception.stomp.StompErrorHandler;
import rednosed.app.intercepter.pre.StompPreHandler;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final StompPreHandler stompPreHandler;
    private final StompErrorHandler stompErrorHandler;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-connection")
                .setAllowedOriginPatterns("*")
                .withSockJS();

        registry.addEndpoint("/ws-stamp")
                .setAllowedOriginPatterns("*")
                .withSockJS();
        registry.setErrorHandler(stompErrorHandler);

        //TODO stomp origin 바꾸기
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/subscribe");  //메세지 브로커가 메시지를 뿌릴때
        registry.setApplicationDestinationPrefixes("/topic"); //클라에서 메세지 브로커한테 메시지를 보낼때
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(stompPreHandler);
    }
}