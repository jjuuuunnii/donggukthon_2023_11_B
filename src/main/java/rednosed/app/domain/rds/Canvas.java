package rednosed.app.domain.rds;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "canvas")
public class Canvas {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "canvas_id")
    @SequenceGenerator(name = "canvas_id", sequenceName = "canvas_id")
    @Column(name = "canvas_id")
    private Long id;

    @OneToMany(mappedBy = "canvas", cascade = CascadeType.MERGE)
    private List<User> userList = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "room_maker_id")
    private User roomMaker;

    private LocalDateTime createdAt;

    @Builder
    private Canvas(List<User> userList, User roomMaker, LocalDateTime createdAt) {
        this.userList = userList;
        this.roomMaker = roomMaker;
        this.createdAt = createdAt;
    }
}
