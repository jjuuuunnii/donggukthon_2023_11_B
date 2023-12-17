package rednosed.app.domain.rds;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "stamp")
public class Stamp {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "stamp_id")
    @SequenceGenerator(name = "stamp_id", sequenceName = "stamp_id")
    @Column(name = "stamp_id")
    private Long id;

    private String stampClientId;
    private String stampName;
    private String stampImgUrl;

    private LocalDateTime createdAt;
}
