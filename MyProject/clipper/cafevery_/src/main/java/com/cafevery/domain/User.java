package com.cafevery.domain;

import com.cafevery.dto.type.ESocialType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "serial_id", nullable = false, unique = true)
    private String serialId; //socialId

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "profile_img_url")
    private String profileImgUrl;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(name = "social_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ESocialType eSocialType;

    @Builder
    public User(String serialId, String nickname, String profileImgUrl, ESocialType eSocialType) {
        this.serialId = serialId;
        this.nickname = nickname;
        this.profileImgUrl = profileImgUrl;
        this.eSocialType = eSocialType;
    }

    public static User of(String serialId, String nickname, ESocialType eSocialType) {
        return User.builder()
                .serialId(serialId)
                .nickname(nickname)
                .profileImgUrl("https://cafevery-bucket.s3.ap-northeast-2.amazonaws.com/default_profile_img.png")
                .eSocialType(eSocialType)
                .build();
    }


}
