package com.example.post.dto.response;

import com.example.common.entity.dto.ResponseDTO;
import com.example.post.dto.PostDTO;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@ToString
public class GetPostResponseDTO extends ResponseDTO {
    private PostDTO postDTO;

    public GetPostResponseDTO(String code, String message, PostDTO postDTO) {
        super(code, message);
        this.postDTO = postDTO;
    }

    public static ResponseEntity<GetPostResponseDTO> success(String message, PostDTO postDTO) {
        GetPostResponseDTO result =  new GetPostResponseDTO("success", message, postDTO);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
