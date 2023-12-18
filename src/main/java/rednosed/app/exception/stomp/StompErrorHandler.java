package rednosed.app.exception.stomp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.StompSubProtocolErrorHandler;

import java.nio.charset.StandardCharsets;

@Component
@Slf4j
public class StompErrorHandler extends StompSubProtocolErrorHandler {

    @Override
    public Message<byte[]> handleErrorMessageToClient(Message<byte[]> errorMessage) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(errorMessage);

        String payloadString = new String(errorMessage.getPayload(), StandardCharsets.UTF_8);

        accessor.setLeaveMutable(true);
        return MessageBuilder.createMessage(payloadString.getBytes(StandardCharsets.UTF_8), accessor.getMessageHeaders());
    }
}
