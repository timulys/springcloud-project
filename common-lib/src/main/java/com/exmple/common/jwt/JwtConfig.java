package com.exmple.common.jwt;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(JwtProperties.class)
public class JwtConfig {
    @Bean
    public JwtUtil jwtUtil(JwtProperties jwtProperties) {
        return new JwtUtil(
                jwtProperties.getSecret(),
                jwtProperties.getAccessTokenValidity(),
                jwtProperties.getRefreshTokenValidity()
        );
    }
}
