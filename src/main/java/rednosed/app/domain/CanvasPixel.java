package rednosed.app.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "canvas_pixel")
public class CanvasPixel {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "canvas_pixel_id")
    @SequenceGenerator(name = "canvas_pixel_id", sequenceName = "canvas_pixel_id")
    @Column(name = "canvas_pixel_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "canvas_id")
    private Canvas canvas;

    private int xCoordinate;
    private int yCoordinate;

    private String color;

    //첫 생성시 null
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime createdAt;
}
