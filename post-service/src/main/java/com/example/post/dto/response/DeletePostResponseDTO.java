package com.example.post.dto.response;

import com.example.common.entity.dto.ResponseDTO;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@ToString
public class DeletePostResponseDTO extends ResponseDTO {
    public DeletePostResponseDTO(String code, String message) {
        super(code, message);
    }

    public static ResponseEntity<DeletePostResponseDTO> success(String message) {
        DeletePostResponseDTO result =  new DeletePostResponseDTO("success", message);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    public static ResponseEntity<DeletePostResponseDTO> fail(String message) {
        DeletePostResponseDTO result =  new DeletePostResponseDTO("fail", message);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }
}
