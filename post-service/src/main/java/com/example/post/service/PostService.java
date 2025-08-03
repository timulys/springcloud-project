package com.example.post.service;

import com.example.post.dto.PostDTO;
import com.example.post.dto.UserDTO;
import com.example.post.dto.request.PageRequestDTO;
import com.example.post.dto.request.PostRegisterRequestDTO;
import com.example.post.dto.request.UpdatePostRequestDTO;
import com.example.post.dto.response.*;
import com.example.post.entity.Post;
import com.example.post.entity.PostFile;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PostService {
    ResponseEntity<? super PostRegisterResponseDTO> register(UserDTO userDTO, PostRegisterRequestDTO requestDTO);

    ResponseEntity<? super GetPostResponseDTO> retrieve(Long id);
    PageResponseDTO<PostDTO> searchList(PageRequestDTO requestDTO);
    PageResponseDTO<PostDTO> searchMyPostList(UserDTO userDTO, PageRequestDTO requestDTO);

    ResponseEntity<? super UpdatePostResponseDTO> update(UserDTO userDTO, UpdatePostRequestDTO requestDTO);

    ResponseEntity<? super DeletePostResponseDTO> delete(UserDTO userDTO, Long id);

    default PostDTO entityToDTO(Post post) {
        PostDTO postDTO = PostDTO.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .authorName(post.getAuthorName())
                .authorEmail(post.getAuthorEmail())
                .authorRole(post.getAuthorRole())
                .createdAt(post.getCreatedAt().toLocalDate().toString())
                .updatedAt(post.getUpdatedAt().toLocalDate().toString())
                .build();

        List<PostFile> fileNames = post.getFileNames();
        if (fileNames != null && !fileNames.isEmpty()) {
            postDTO.setUploadFileNames(fileNames.stream().map(PostFile::getFileName).toList());
        }

        return postDTO;
    }
}
