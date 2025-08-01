package com.example.post.service.impl;

import com.example.post.dto.UserDTO;
import com.example.post.dto.request.PostRegisterRequestDTO;
import com.example.post.dto.response.PostRegisterResponseDTO;
import com.example.post.entity.Post;
import com.example.post.repository.PostRepository;
import com.example.post.service.PostService;
import com.example.post.util.CustomFileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Log4j2
@Service
@Transactional
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    // Autowired Components
    private final PostRepository postRepository;
    private final CustomFileUtil customFileUtil;

    @Override
    public ResponseEntity<? super PostRegisterResponseDTO> register(UserDTO userDTO, PostRegisterRequestDTO requestDTO) {
        // 등록된 파일이 있다면 업로드
        if (requestDTO.getFiles() != null && !requestDTO.getFiles().isEmpty()) {
            List<MultipartFile> files = requestDTO.getFiles();
            List<String> uploadFileNames = customFileUtil.saveFiles(files);
            requestDTO.setUploadFileNames(uploadFileNames);
            log.info("Upload File Names : {}",  uploadFileNames);
        }

        Post post = Post.builder()
                .title(requestDTO.getTitle())
                .content(requestDTO.getContent())
                .authorEmail(userDTO.getEmail())
                .authorName(userDTO.getName())
                .authorRole(userDTO.getRole())
                .build();

        List<String> uploadFileNames = requestDTO.getUploadFileNames();
        if (uploadFileNames != null && !uploadFileNames.isEmpty()) {
            uploadFileNames.forEach(post::addImageString);
        }
        log.info("Register Post : {}", post);
        Long postId = postRepository.save(post).getId();

        return PostRegisterResponseDTO.success("게시글 등록 완료", postId);
    }
}
