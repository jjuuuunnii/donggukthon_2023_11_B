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
@Table(name = "like_stamp_id")
public class LikeStamp {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "like_stamp_id")
    @SequenceGenerator(name = "like_stamp_id", sequenceName = "like_stamp_id")
    @Column(name = "like_stamp_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stamp_id")
    private Stamp stamp;

    private LocalDateTime createdAt;
}
