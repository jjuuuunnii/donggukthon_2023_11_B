package rednosed.app.domain.rds;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_stamp")
public class UserStamp {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_stamp_id")
    @SequenceGenerator(name = "user_stamp_id", sequenceName = "user_stamp_id")
    @Column(name = "user_stamp_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stamp_id")
    private Stamp stamp;

    private LocalDateTime createdAt;

    @Builder
    private UserStamp(User user, Stamp stamp, LocalDateTime createdAt) {
        this.user = user;
        this.stamp = stamp;
        this.createdAt = createdAt;
    }


}