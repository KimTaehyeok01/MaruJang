package com.marujang.global.security.oauth2;

import com.marujang.global.security.jwt.JwtTokenProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	private static final String FRONTEND_REDIRECT_URI = "http://localhost:5173/oauth2/redirect";

	private final JwtTokenProvider jwtTokenProvider;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {

		// TODO: authentication.getPrincipal() 에서 로컬 User(id, role) 를 조회/추출
		// TODO: jwtTokenProvider.createAccessToken(userId, role) 로 토큰 발급
		// TODO: 토큰을 쿼리 파라미터로 붙여 FRONTEND_REDIRECT_URI 로 리다이렉트

		getRedirectStrategy().sendRedirect(request, response, FRONTEND_REDIRECT_URI);
	}
}
