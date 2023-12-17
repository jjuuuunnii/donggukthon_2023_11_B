package rednosed.app.domain.nosql;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.awt.Color;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Pixel {

    /* canvas는 13 x 13이 default 값 */
    private String canvasId;
    private List<List<Color>> colors;  // 이차원 리스트로 색상 관리
    private String lastModifiedUserId;
    private LocalDateTime createdAt;

    @Builder
    public Pixel(String canvasId, List<List<Color>> colors, String lastModifiedUserId, LocalDateTime createdAt) {
        this.canvasId = canvasId;
        this.colors = colors;
        this.lastModifiedUserId = lastModifiedUserId;
        this.createdAt = createdAt;
    }

    // 특정 위치의 색상을 설정하는 메소드
    public void setColor(int x, int y, Color color) {
        while (colors.size() <= x) {
            colors.add(new ArrayList<>());
        }
        List<Color> row = colors.get(x);
        while (row.size() <= y) {
            row.add(null);
        }
        row.set(y, color);
    }

    // 특정 위치의 색상을 가져오는 메소드
    public Color getColor(int x, int y) {
        if (x < colors.size() && y < colors.get(x).size()) {
            return colors.get(x).get(y);
        }
        return null;  // 색상이 설정되지 않은 경우
    }
}
