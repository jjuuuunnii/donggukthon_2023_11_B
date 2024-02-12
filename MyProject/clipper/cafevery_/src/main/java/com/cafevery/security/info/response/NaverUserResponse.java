package com.cafevery.security.info.response;

import com.cafevery.dto.type.ESocialType;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class NaverUserResponse implements UserResponse {

    private String resultcode;
    private String message;
    private Response response;

    @Builder
    public NaverUserResponse(String resultcode, String message, Response response) {
        this.resultcode = resultcode;
        this.message = message;
        this.response = response;
    }

    @Getter
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Response{
        private String id;
        private String nickname;
        private String name;
        private String email;
        private String gender;
        private String age;
        private String birthday;
        private String profileImage;
        private String birthyear;
        private String mobile;
    }

    @Override
    public ESocialType supportServer(){
        return ESocialType.NAVER;
    }

    @Override
    public String getId(){
        return response.getId();
    }

    @Override
    public String getName(){
        return response.getName();
    }

    @Override
    public Class<? extends UserResponse> getImplementationClass() {
        return NaverUserResponse.class;
    }
}
