package rednosed.app.security.oauth.service;


import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import rednosed.app.domain.rds.User;
import rednosed.app.repository.rds.UserRepository;
import rednosed.app.security.oauth.info.CustomOAuth2user;
import rednosed.app.security.oauth.info.KakaoUserInfo;
import rednosed.app.service.UserService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OAuth2UserService extends DefaultOAuth2UserService {


    private final UserService userService;
    private final UserRepository userRepository;


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        KakaoUserInfo kakaoUserInfo = new KakaoUserInfo(attributes);

        User user = checkUser(kakaoUserInfo);

        return new CustomOAuth2user(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                attributes,
                userNameAttributeName,
                kakaoUserInfo.getId(),
                user.getUserClientId()
        );
    }

    private User checkUser(KakaoUserInfo kakaoUserInfo) {

        Optional<User> tmpUser = userRepository.findBySocialId(kakaoUserInfo.getId());
        if (tmpUser.isPresent()) {
            return tmpUser.get();
        }

//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
//        String formattedDate = LocalDateTime.now().format(formatter);

        User user = User.builder()
                .userClientId(UUID.randomUUID().toString())
                .nickname(null)
                .refreshToken(null)
                .socialId(kakaoUserInfo.getId())
                .sealOrderCount(0)
                .canvas(null)
                .role("GUEST")
                .createdAt(LocalDateTime.now())
                .build();

        return userService.save(user);
    }
}

