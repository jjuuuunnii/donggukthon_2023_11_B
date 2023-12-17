package rednosed.app.domain.nosql;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collation = "canvas_pixel")
public class CanvasPixel {

    @Id
    private String id;

    private String canvasId;
    private int xCoordinate;
    private int yCoordinate;
    private String color;

    private String lastModifiedUserId;
    private LocalDateTime createdAt;
}
