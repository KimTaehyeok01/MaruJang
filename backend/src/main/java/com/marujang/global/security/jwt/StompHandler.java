package com.marujang.global.security.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StompHandler implements ChannelInterceptor {

	private final JwtTokenProvider jwtTokenProvider;

	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

		// CONNECT 명령일 때만 JWT를 검증. 이후 SEND/SUBSCRIBE는 이미 연결된 세션이라 재검증 불필요
		if (StompCommand.CONNECT.equals(accessor.getCommand())) {
			String authHeader = accessor.getFirstNativeHeader("Authorization");
			if (authHeader == null || !authHeader.startsWith("Bearer ")) {
				throw new IllegalArgumentException("WebSocket 연결에 Authorization 헤더가 필요합니다.");
			}
			String token = authHeader.substring(7);
			if (!jwtTokenProvider.isValid(token)) {
				throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
			}
		}

		return message;
	}
}
