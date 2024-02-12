package com.cafevery.dto.type;

import static java.util.Locale.ENGLISH;

public enum ESocialType {


    KAKAO,
    NAVER,
    GOOGLE,
    DEFAULT,
    ;

    public static ESocialType fromName(String type) {
        return ESocialType.valueOf(type.toUpperCase(ENGLISH));
    }
}
