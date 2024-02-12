package com.cafevery.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "likes_cafe")
public class likesCafe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @JoinColumn(name = "cafe_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Cafe cafe;

    @Builder
    public likesCafe(User user, Cafe cafe) {
        this.user = user;
        this.cafe = cafe;
    }

    public static likesCafe of(User user, Cafe cafe) {
        return likesCafe.builder()
                .user(user)
                .cafe(cafe)
                .build();
    }
}
