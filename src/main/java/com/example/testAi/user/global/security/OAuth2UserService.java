package com.example.testAi.user.global.security;
import com.example.testAi.user.domain.member.MemberService;
import com.example.testAi.user.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
@Slf4j
public class OAuth2UserService extends DefaultOAuth2UserService {
    private final MemberService memberService;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        Map<String, Object> attributes = oAuth2User.getAttributes();

        String oauthId = oAuth2User.getName();

        String oauthType = userRequest.getClientRegistration().getRegistrationId().toUpperCase();

        switch (oauthType) {
            case "KAKAO" -> {
                Map attributesProperties = (Map) attributes.get("properties");
                String nickname = (String) attributesProperties.get("nickname");
                String profileImgUrl = (String) attributesProperties.get("profile_image");
                String username = "KAKAO_%s".formatted(oauthId);
                String email = oauthId + "@kakao.com";

                Member member = memberService.modifyOrJoin(username, oauthType, email, nickname, profileImgUrl).getData();

                return new SecurityUser(member.getUsername(), member.getPassword(), member.getAuthorities());

            }
        }

        throw new OAuth2AuthenticationException("지원되지 않은 공급자 입니다.");
    }
}