package com.cafevery.utility;

import com.cafevery.dto.type.ESocialType;
import com.cafevery.dto.type.ErrorCode;
import com.cafevery.exception.CommonException;
import com.cafevery.security.info.response.GoogleUserResponse;
import com.cafevery.security.info.response.KakaoUserResponse;
import com.cafevery.security.info.response.NaverUserResponse;
import com.cafevery.security.info.response.UserResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;



@Slf4j
@Component
public class OAuthUserClientUtil {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public UserResponse getResponse(ESocialType socialType, Map<String, Object> attributes) {
        Class<? extends UserResponse> responseClass = determineResponseClass(socialType);
        return objectMapper.registerModule(new JavaTimeModule()).convertValue(attributes, responseClass);
    }

    private Class<? extends UserResponse> determineResponseClass(ESocialType socialType) {
        return switch (socialType) {
            case KAKAO -> KakaoUserResponse.class;
            case NAVER -> NaverUserResponse.class;
            case GOOGLE -> GoogleUserResponse.class;
            default -> throw new CommonException(ErrorCode.NOT_FOUND_SOCIAL_TYPE);
        };
    }
}