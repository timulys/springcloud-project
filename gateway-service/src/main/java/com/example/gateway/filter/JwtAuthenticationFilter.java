package com.example.gateway.filter;

import com.example.gateway.config.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements WebFilter {
    private final JwtConfig jwtConfig;
    private static final String BEARER = "Bearer ";
    private static final List<String> PUBLIC_PATHS = List.of("/auth/signup", "/auth/login"); // 인증 없이 통과할 경로 패턴

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String path = exchange.getRequest().getPath().toString();

        // 인증 제외 경로 설정
        if (PUBLIC_PATHS.stream().anyMatch(path::startsWith)) {
            return chain.filter(exchange);
        }

        // Authorization 헤더가 없거나 Bearer 토큰이 아니면 401
        return Mono.justOrEmpty(exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION))
                .filter(h -> h.startsWith(BEARER))
                .map(h -> h.substring(BEARER.length()))
                // 토큰 검증 및 Claims 추출(downstream 전달)
                .flatMap(token -> validateAndForward(token, exchange, chain))
                // 중간에 빈 값이 오면 unauthorized 처리
                .switchIfEmpty(unauthorized(exchange));
    }

    private Mono<Void> validateAndForward(String token, ServerWebExchange exchange, WebFilterChain chain) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(jwtConfig.getSecretKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            ServerHttpRequest request = exchange.getRequest().mutate()
                    .header("X-User-Email", claims.get("email", String.class))
                    .header("X-User-Name", claims.get("name", String.class))
                    .header("X-User-Role", claims.get("role", String.class))
                    .build();
            return chain.filter(exchange.mutate().request(request).build());
        } catch (JwtException ex) {
            return unauthorized(exchange);
        }
    }

    // 비인가 회원 중복 응답값 처리
    private Mono<Void> unauthorized(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }
}
