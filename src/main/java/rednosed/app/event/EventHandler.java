package rednosed.app.event;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import rednosed.app.dto.response.StampIdDto;

@Component
@RequiredArgsConstructor
public class EventHandler {

    private final SimpMessagingTemplate simpMessagingTemplate;

    @EventListener(LoadingEvent.class)
    public void handleLoadingEvent(LoadingEvent loadingEvent) {
        simpMessagingTemplate.convertAndSend("/subscribe/stamp/" + loadingEvent.getStampId(),
                StampIdDto.builder()
                        .stampId(loadingEvent.getStampId())
                        .build()
                );
    }
}
