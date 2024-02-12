package com.cafevery.security.service;


import com.cafevery.domain.User;
import com.cafevery.dto.type.ERole;
import com.cafevery.dto.type.ESocialType;
import com.cafevery.repository.UserRepository;
import com.cafevery.security.info.CustomOAuth2User;
import com.cafevery.security.info.response.UserResponse;
import com.cafevery.service.UserService;
import com.cafevery.utility.OAuthUserClientUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final UserService userService;
    private final OAuthUserClientUtil oAuthUserClientUtil;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        ESocialType eSocialType = ESocialType.fromName(userRequest.getClientRegistration().getClientName());
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();

        UserResponse response = oAuthUserClientUtil.getResponse(eSocialType, attributes);
        log.info("response: {}", response.toString());
        User user = userRepository.findBySerialId(response.getId())
                .orElseGet(() -> userService.saveUser(User.of(response.getId(), response.getName(), response.supportServer())));

        return new CustomOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(ERole.USER.name())),
                attributes,
                userNameAttributeName,
                user.getId()
        );
    }
}
