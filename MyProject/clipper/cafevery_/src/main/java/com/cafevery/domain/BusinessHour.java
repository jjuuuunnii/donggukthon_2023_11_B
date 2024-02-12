package com.cafevery.domain;


import com.cafevery.dto.type.EDay;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "`business_hour`")
public class BusinessHour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "day")
    @Enumerated(EnumType.STRING)
    private EDay day;

    @JoinColumn(name = "cafe_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Cafe cafe;

    @Builder
    public BusinessHour(LocalDateTime startTime, LocalDateTime endTime, EDay day, Cafe cafe) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.day = day;
        this.cafe = cafe;
    }

    public static BusinessHour of(LocalDateTime startTime, LocalDateTime endTime, EDay day, Cafe cafe) {
        return BusinessHour.builder()
                .startTime(startTime)
                .endTime(endTime)
                .day(day)
                .cafe(cafe)
                .build();
    }
}
