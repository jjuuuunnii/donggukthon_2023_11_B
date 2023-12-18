package rednosed.app.security.oauth.info;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Collection;
import java.util.Map;

@Getter
public class CustomOAuth2user extends DefaultOAuth2User {

    private final String socialId;
    private final String UserClientId;

    /**
     * Constructs a {@code DefaultOAuth2User} using the provided parameters.
     *
     * @param authorities      the authorities granted to the user
     * @param attributes       the attributes about the user
     * @param nameAttributeKey the key used to access the user's &quot;name&quot; from
     *                         {@link #getAttributes()}
     */

    public CustomOAuth2user(
            Collection<? extends GrantedAuthority> authorities,
            Map<String, Object> attributes,
            String nameAttributeKey, String socialId, String UserClientId) {
        super(authorities, attributes, nameAttributeKey);
        this.socialId = socialId;
        this.UserClientId = UserClientId;
    }
}
