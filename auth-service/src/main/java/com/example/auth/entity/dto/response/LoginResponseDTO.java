package com.example.auth.entity.dto.response;

import com.exmple.common.entity.dto.ResponseDTO;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ToString
public class LoginResponseDTO extends ResponseDTO {
    private String accessToken;
    private String refreshToken;

    public LoginResponseDTO(String code, String message, String accessToken, String refreshToken) {
        super(code, message);
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public static ResponseEntity<? super LoginResponseDTO> success(String message, String accessToken, String refreshToken) {
        LoginResponseDTO result = new LoginResponseDTO("success", message, accessToken, refreshToken);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
