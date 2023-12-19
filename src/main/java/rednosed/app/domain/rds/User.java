package rednosed.app.domain.rds;

import jakarta.persistence.*;
import lombok.*;

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
    private String role;

    // User : Canvas => Many to one
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "canvas_id")
    private Canvas canvas;

    private LocalDateTime createdAt;


    @Builder
    private User(String userClientId, String nickname, String refreshToken, String socialId, int sealOrderCount, String role, Canvas canvas, LocalDateTime createdAt){
        this.userClientId = userClientId;
        this.nickname = nickname;
        this.refreshToken = refreshToken;
        this.socialId = socialId;
        this.sealOrderCount = sealOrderCount;
        this.role = role;
        this.canvas = canvas;
        this.createdAt = createdAt;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void updateUserNicknameAndRole(String nickname, String role) {
        this.nickname = nickname;
        this.role = role;
    }

    public void updateCanvas(Canvas canvas) {
        this.canvas = canvas;
    }

    public void updateOrderCount(int sealOrderCount) {
        this.sealOrderCount = sealOrderCount;
    }
}
