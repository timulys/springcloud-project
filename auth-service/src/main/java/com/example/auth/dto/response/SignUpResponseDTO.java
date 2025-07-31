package com.example.auth.dto.response;

import com.example.common.entity.dto.ResponseDTO;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@ToString
public class SignUpResponseDTO extends ResponseDTO {
    public SignUpResponseDTO(String code, String message) {
        super(code, message);
    }

    public static ResponseEntity<? super SignUpResponseDTO> success(String message) {
        SignUpResponseDTO result = new SignUpResponseDTO("success", message);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
