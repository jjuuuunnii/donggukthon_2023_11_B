package rednosed.app.domain.rds;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "like_seal")
public class LikeSeal {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "like_seal_id")
    @SequenceGenerator(name = "like_seal_id", sequenceName = "like_seal_id")
    @Column(name = "like_seal_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seal_id")
    private Seal seal;

    private LocalDateTime createdAt;

    @Builder
    private LikeSeal(User user, Seal seal, LocalDateTime createdAt) {
        this.user = user;
        this.seal = seal;
        this.createdAt = createdAt;
    }
}
