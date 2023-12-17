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
}