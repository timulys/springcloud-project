package com.example.post.controller;

import com.example.post.dto.PostDTO;
import com.example.post.dto.UserDTO;
import com.example.post.dto.request.PageRequestDTO;
import com.example.post.dto.request.PostRegisterRequestDTO;
import com.example.post.dto.request.UpdatePostRequestDTO;
import com.example.post.dto.response.*;
import com.example.post.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;

    @PostMapping(value = "/register")
    public ResponseEntity<? super PostRegisterResponseDTO> register(
            @RequestHeader("X-User-Email") String email,
            @RequestHeader("X-User-Name") String name,
            @RequestHeader("X-User-Role") String role,
            @Valid PostRegisterRequestDTO postRegisterRequestDTO) { /* multipart/form-data 요청은 HTTP 메시지 바디 전체가 아니라 “폼 필드 + 파일” 형태로 인코딩되기 때문에, @RequestBody (즉, JSON 바디 바인딩) 으로는 받을 수 없습니다. 스프링 MVC에서는 기본적으로 폼 데이터 바인딩을 할 때 @ModelAttribute를 사용하도록 되어 있고, 애노테이션을 생략해도 디폴트로 적용됩니다. */
        UserDTO userDTO = new UserDTO(email, name, role);
        return postService.register(userDTO, postRegisterRequestDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<? super GetPostResponseDTO> retrieve(@PathVariable("id") Long id) {
        return postService.retrieve(id);
    }

    @GetMapping("/list")
    public PageResponseDTO<PostDTO> list(PageRequestDTO requestDTO) {
        return postService.searchList(requestDTO);
    }

    @GetMapping("/list/my")
    public PageResponseDTO<PostDTO> listMyPosts(
            @RequestHeader("X-User-Email") String email,
            @RequestHeader("X-User-Name") String name,
            @RequestHeader("X-User-Role") String role,
            PageRequestDTO requestDTO) {
        UserDTO userDTO = new UserDTO(email, name, role);
        return postService.searchMyPostList(userDTO, requestDTO);
    }

    @PutMapping("/update")
    public ResponseEntity<? super UpdatePostResponseDTO> update(
            @RequestHeader("X-User-Email") String email,
            @RequestHeader("X-User-Name") String name,
            @RequestHeader("X-User-Role") String role,
            @Valid UpdatePostRequestDTO requestDTO) {
        UserDTO userDTO = new UserDTO(email, name, role);
        return postService.update(userDTO, requestDTO);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<? super DeletePostResponseDTO> delete(
            @RequestHeader("X-User-Email") String email,
            @RequestHeader("X-User-Name") String name,
            @RequestHeader("X-User-Role") String role,
            @PathVariable("id") Long id) {
        UserDTO userDTO = new UserDTO(email, name, role);
        return postService.delete(userDTO, id);
    }
}
