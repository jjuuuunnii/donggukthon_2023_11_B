package rednosed.app.domain.rds;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_id")
    @SequenceGenerator(name = "users_id", sequenceName = "users_id")
    @Column(name = "users_id")
    private Long id;

    private String userClientId;
    private String nickname;
    private String refreshToken;
    private String socialId;
    private int sealOrderCount;

    // User : Canvas => Many to one
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "canvas_id")
    private Canvas canvas;

    private LocalDateTime createdAt;


    @Builder
    private User(String userClientId, String nickname, String refreshToken, String socialId, int sealOrderCount, Canvas canvas, LocalDateTime createdAt){
        this.userClientId = userClientId;
        this.nickname = nickname;
        this.refreshToken = refreshToken;
        this.socialId = socialId;
        this.sealOrderCount = sealOrderCount;
        this.canvas = canvas;
        this.createdAt = createdAt;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void updateUserNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updateCanvas(Canvas canvas) {
        this.canvas = canvas;
    }

    public void updateOrderCount(int sealOrderCount) {
        this.sealOrderCount = sealOrderCount;
    }
}
