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
public class GoogleUserResponse implements UserResponse{

    private String email;
    private String verified_email;
    private String name;
    private String given_name;
    private String family_name;
    private String picture;
    private String locale;
    private String sub;

    @Builder
    public GoogleUserResponse(String email, String verified_email, String name, String given_name, String family_name, String picture, String locale, String sub) {
        this.email = email;
        this.verified_email = verified_email;
        this.name = name;
        this.given_name = given_name;
        this.family_name = family_name;
        this.picture = picture;
        this.locale = locale;
        this.sub = sub;
    }

    @Override
    public ESocialType supportServer(){
        return ESocialType.GOOGLE;
    }

    @Override
    public String getId(){
        return sub;
    }

    @Override
    public String getName(){
        return name;
    }

    @Override
    public Class<? extends UserResponse> getImplementationClass() {
        return GoogleUserResponse.class;
    }
}
