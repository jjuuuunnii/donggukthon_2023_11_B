package rednosed.app.domain.nosql;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import rednosed.app.contrant.Constants;

import java.awt.Color;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Pixel {

    /* canvas는 13 x 13이 default 값 */
    private List<List<String>> colors;  // 이차원 리스트로 색상 관리
    private String canvasClientId;
    private LocalDateTime createdAt;

    @Builder
    public Pixel(String canvasClientId, List<List<String>> colors, LocalDateTime createdAt) {
        this.canvasClientId = canvasClientId;
        this.colors = colors;
        this.createdAt = createdAt;
    }

    public void updateColor(int x, int y, String color) {
        while (colors.size() <= x) {
            colors.add(new ArrayList<>());
        }
        List<String> row = colors.get(x);
        while (row.size() <= y) {
            row.add(null);
        }
        row.set(y, color);
    }


    public String getColor(int x, int y) {
        if (x < colors.size() && y < colors.get(x).size()) {
            return colors.get(x).get(y);
        }
        return null;
    }

    public long getLeftTime() {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(createdAt, now);
        long secondsPassed = duration.getSeconds();
        long totalSeconds = Constants.TOTAL_CANVAS_MAKING_TIME; // 예시로 10분 (600초)을 설정

        return totalSeconds - secondsPassed; // 남은 시간을 초 단위로 반환
    }
}
