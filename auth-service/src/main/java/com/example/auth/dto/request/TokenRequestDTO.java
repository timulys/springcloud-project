package com.example.auth.dto.request;

import lombok.Data;

@Data
public class TokenRequestDTO {
    private String accessToken;
    private String refreshToken;
}
