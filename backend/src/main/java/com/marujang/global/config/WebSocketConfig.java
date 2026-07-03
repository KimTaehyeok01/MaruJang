package com.marujang.global.config;

import com.marujang.global.security.jwt.StompHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	private final StompHandler stompHandler;

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		// /topic/** 구독 → 서버가 클라이언트로 메시지를 브로드캐스트하는 경로
		registry.enableSimpleBroker("/topic");
		// /app/** 로 시작하는 요청 → @MessageMapping 메서드로 라우팅
		registry.setApplicationDestinationPrefixes("/app");
	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		// 클라이언트가 WebSocket 연결을 맺는 엔드포인트. SockJS 폴백 지원
		registry.addEndpoint("/ws")
				.setAllowedOriginPatterns("http://localhost:5173")
				.withSockJS();
	}

	@Override
	public void configureClientInboundChannel(ChannelRegistration registration) {
		// STOMP CONNECT 시 JWT 검증을 StompHandler 에서 처리
		registration.interceptors(stompHandler);
	}
}
