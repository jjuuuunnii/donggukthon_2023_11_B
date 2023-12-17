package rednosed.app.domain.rds;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
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
}
