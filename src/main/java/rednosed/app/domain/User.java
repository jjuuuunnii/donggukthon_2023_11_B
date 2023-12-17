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

    // User : Canvas => Many to one
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "canvas_id")
    private Canvas canvas;

    private LocalDateTime createdAt;

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void updateUserNickname(String nickname) {
        this.nickname = nickname;
    }
}
