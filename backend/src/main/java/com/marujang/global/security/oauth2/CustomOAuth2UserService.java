package com.marujang.global.security.oauth2;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User oAuth2User = super.loadUser(userRequest);

		// TODO: registrationId(google/kakao)별로 attributes 파싱 방식이 다름 — provider별 파서 분리 필요
		// TODO: email 로 기존 User 조회, 없으면 신규 가입 처리(최초 로그인 시 MERCHANT/SUPPLIER 선택 플로우 필요)

		return oAuth2User;
	}
}
