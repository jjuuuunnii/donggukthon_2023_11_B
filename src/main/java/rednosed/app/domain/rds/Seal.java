package rednosed.app.domain.rds;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "seals")
public class Seal {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seals_id")
    @SequenceGenerator(name = "seals_id", sequenceName = "seals_id")
    @Column(name = "seals_id")
    private Long id;

    private String sealClientId;
    private String sealName;
    private String sealImgUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime createdAt;

    @Builder
    private Seal(String sealClientId, User user, String sealName, String sealImgUrl, LocalDateTime createdAt) {
        this.sealClientId = sealClientId;
        this.user = user;
        this.sealName = sealName;
        this.sealImgUrl = sealImgUrl;
        this.createdAt = createdAt;
    }
}
