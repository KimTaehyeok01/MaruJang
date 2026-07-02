// application-secret.yml 의 jwt.* 값을 바인딩하는 설정 클래스
package com.marujang.global.security.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
public record JwtProperties(
		String secret,
		long accessTokenExpiration,
		long refreshTokenExpiration
) {
}
