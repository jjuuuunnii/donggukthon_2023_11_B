package rednosed.app.security.oauth.info;


import java.util.Map;


public record KakaoUserInfo(Map<String, Object> attributes) {
    public String getId() {
        return String.valueOf(attributes.get("id"));
    }
}
