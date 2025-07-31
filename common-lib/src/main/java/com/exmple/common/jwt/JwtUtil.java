package com.exmple.common.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import java.security.Key;
import java.util.Date;
import java.util.Map;

@Slf4j
public class JwtUtil {
    private final Key key;
    private final long accessTokenValidityInMilliseconds;
    private final long refreshTokenValidityInMilliseconds;

    public JwtUtil(String secretKey, long accessTokenValidityInMilliseconds, long refreshTokenValidityInMilliseconds) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
        this.accessTokenValidityInMilliseconds = accessTokenValidityInMilliseconds;
        this.refreshTokenValidityInMilliseconds = refreshTokenValidityInMilliseconds;
    }

    // Access Token 생성
    public String generateAccessToken(String subject, Map<String, Object> claims) {
        return generateToken(subject, claims, accessTokenValidityInMilliseconds);
    }

    // Refresh Token 생성
    public String generateRefreshToken(String subject) {
        return generateToken(subject, null, refreshTokenValidityInMilliseconds);
    }

    // Token 생성
    private String generateToken(String subject, Map<String, Object> claims, long validityInMilliseconds) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + validityInMilliseconds); // TODO : 만료 시간 확인해볼 것(정확히 되는지)

        JwtBuilder builder = Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key, SignatureAlgorithm.HS512);

        if (claims != null) {
            builder.setClaims(claims);
        }

        return builder.compact();
    }

    // 토큰에서 subjects 추출
    public Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            log.warn("JWT expired: {}", token);
            return e.getClaims();
        } catch (Exception e) {
            log.error("JWT parsing failed: {}", token);
            throw e;
        }
    }

    // 유효성 검증 로직
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("Invalid JWT: {}", e.getMessage());
            return false;
        }
    }

    // 만료 여부 확인
    public boolean isTokenExpired(String token) {
        return parseClaims(token).getExpiration().before(new Date());
    }
}
