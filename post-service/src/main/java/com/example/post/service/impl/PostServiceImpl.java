package com.example.post.service.impl;

import com.example.common.exception.PostNotFoundException;
import com.example.post.dto.PostDTO;
import com.example.post.dto.UserDTO;
import com.example.post.dto.request.PageRequestDTO;
import com.example.post.dto.request.PostRegisterRequestDTO;
import com.example.post.dto.request.UpdatePostRequestDTO;
import com.example.post.dto.response.*;
import com.example.post.entity.Post;
import com.example.post.entity.PostFile;
import com.example.post.repository.PostRepository;
import com.example.post.service.PostService;
import com.example.post.util.CustomFileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
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

    @Override
    public ResponseEntity<? super GetPostResponseDTO> retrieve(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new PostNotFoundException(id));
        PostDTO postDTO = entityToDTO(post);
        return GetPostResponseDTO.success("게시글 단건 조회", postDTO);
    }

    @Override
    public PageResponseDTO<PostDTO> searchList(PageRequestDTO requestDTO) {
        return postRepository.searchList(requestDTO);
    }

    @Override
    public PageResponseDTO<PostDTO> searchMyPostList(UserDTO userDTO, PageRequestDTO requestDTO) {
        requestDTO.setKeywordType("authorName");
        requestDTO.setKeyword(userDTO.getName());
        return postRepository.searchList(requestDTO);
    }

    @Override
    public ResponseEntity<? super UpdatePostResponseDTO> update(UserDTO userDTO, UpdatePostRequestDTO requestDTO) {
        long id = Long.parseLong(requestDTO.getId());
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id));
        post.update(requestDTO.getTitle(), requestDTO.getContent());

        // 이미지 처리
        if (requestDTO.getFiles() != null && !requestDTO.getFiles().isEmpty()) {
            List<MultipartFile> files = requestDTO.getFiles();
            List<String> updateUploadFileNames = customFileUtil.saveFiles(files);

            // 중복 파일 삭제
            List<String> removeFileNames = post.getFileNames().stream()
                    .map(PostFile::getFileName)
                    .toList();
            customFileUtil.deleteFiles(removeFileNames);
            post.clearList();

            updateUploadFileNames.forEach(post::addImageString);
        }

        return UpdatePostResponseDTO.success("게시글 수정이 완료되었습니다.");
    }

    @Override
    public ResponseEntity<? super DeletePostResponseDTO> delete(UserDTO userDTO, Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new PostNotFoundException(id));
        if (!post.getAuthorEmail().equals(userDTO.getEmail())) {
            return DeletePostResponseDTO.fail("삭제할 수 없는 게시글입니다.");
        }
        // 게시글 삭제
        postRepository.deleteById(id);

        // 게시글에 등록된 파일이 있다면 삭제
        List<String> deleteFiles = new ArrayList<>();

        List<PostFile> fileNames = post.getFileNames();
        if (fileNames != null && !fileNames.isEmpty()) {
            deleteFiles = fileNames.stream().map(PostFile::getFileName).toList();
        }

        customFileUtil.deleteFiles(deleteFiles);

        return DeletePostResponseDTO.success("게시글 삭제가 완료되었습니다.");
    }
}
