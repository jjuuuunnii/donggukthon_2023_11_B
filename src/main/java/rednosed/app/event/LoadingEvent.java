package rednosed.app.event;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoadingEvent {

    private String stampId;

    @Builder
    public LoadingEvent(String stampId) {
        this.stampId = stampId;
    }

}
