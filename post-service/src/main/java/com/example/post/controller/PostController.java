package com.example.post.controller;

import com.example.post.dto.UserDTO;
import com.example.post.dto.request.PostRegisterRequestDTO;
import com.example.post.dto.response.PostRegisterResponseDTO;
import com.example.post.service.PostService;
import com.example.post.util.CustomFileUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;
    private final CustomFileUtil fileUtil;

    @PostMapping(value = "/register")
    public ResponseEntity<? super PostRegisterResponseDTO> register(
            @RequestHeader("X-User-Email") String email,
            @RequestHeader("X-User-Name") String name,
            @RequestHeader("X-User-Role") String role,
            @Valid PostRegisterRequestDTO postRegisterRequestDTO) { /* multipart/form-data 요청은 HTTP 메시지 바디 전체가 아니라 “폼 필드 + 파일” 형태로 인코딩되기 때문에, @RequestBody (즉, JSON 바디 바인딩) 으로는 받을 수 없습니다. 스프링 MVC에서는 기본적으로 폼 데이터 바인딩을 할 때 @ModelAttribute를 사용하도록 되어 있고, 애노테이션을 생략해도 디폴트로 적용됩니다. */
        UserDTO userDTO = new UserDTO(email, name, role);
        return postService.register(userDTO, postRegisterRequestDTO);
    }
}
