package com.example.post.repository.search;

import com.example.post.dto.PostDTO;
import com.example.post.dto.request.PageRequestDTO;
import com.example.post.dto.response.PageResponseDTO;

public interface PostSearch {
    PageResponseDTO<PostDTO> searchList(PageRequestDTO pageRequestDTO);
}
