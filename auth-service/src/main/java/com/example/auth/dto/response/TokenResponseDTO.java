package com.example.auth.dto.response;

import com.example.common.entity.dto.ResponseDTO;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@ToString
public class TokenResponseDTO extends ResponseDTO {
    private String accessToken;
    private String refreshToken;

    public TokenResponseDTO(String code, String message, String accessToken, String refreshToken) {
        super(code, message);
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public static ResponseEntity<? super TokenResponseDTO> success(String message, String accessToken, String refreshToken) {
        TokenResponseDTO result = new TokenResponseDTO("success", message, accessToken, refreshToken);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    public static ResponseEntity<? super TokenResponseDTO> fail(String message) {
        TokenResponseDTO result = new TokenResponseDTO("fail", message, null, null);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
