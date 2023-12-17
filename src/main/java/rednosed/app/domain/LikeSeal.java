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
}
