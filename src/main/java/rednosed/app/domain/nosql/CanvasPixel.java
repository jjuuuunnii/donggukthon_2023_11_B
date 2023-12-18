package rednosed.app.domain.nosql;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(collection = "canvas_pixel")
public class CanvasPixel {

    @Id
    private String id;
    private List<Pixel> pixels;
    private LocalDateTime leftTime;

    @Builder
    private CanvasPixel(List<Pixel> pixels, LocalDateTime leftTime) {
        this.pixels = pixels;
        this.leftTime = leftTime;
    }
}
