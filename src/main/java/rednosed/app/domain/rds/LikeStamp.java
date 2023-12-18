package rednosed.app.domain.rds;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Builder
    private LikeStamp(User user, Stamp stamp, LocalDateTime createdAt) {
        this.user = user;
        this.stamp = stamp;
        this.createdAt = createdAt;
    }
}
