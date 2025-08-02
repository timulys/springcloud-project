package com.example.post.repository.search;

import com.example.post.dto.PostDTO;
import com.example.post.dto.request.PageRequestDTO;
import com.example.post.dto.response.PageResponseDTO;
import com.example.post.entity.Post;
import com.example.post.entity.QPost;
import com.example.post.entity.QPostFile;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.Objects;

@Log4j2
public class PostSearchImpl extends QuerydslRepositorySupport implements PostSearch {
    // 대상 클래스 지정
    public PostSearchImpl() {
        super(Post.class);
    }

    @Override
    public PageResponseDTO<PostDTO> searchList(PageRequestDTO pageRequestDTO) {
        log.info("------------------ Search Post List ------------------");

        Pageable pageable = PageRequest.of(
                pageRequestDTO.getPage() - 1,
                pageRequestDTO.getSize(),
                Sort.by("id").descending());

        QPost post = QPost.post;
        QPostFile postFile = QPostFile.postFile;

        JPQLQuery<Post> query = from(post);
        query.leftJoin(post.fileNames, postFile); // posts - post_file
        query.where(postFile.ord.eq(0));

        // 검색 조건 (제목 or 작성자 이름)
        if (pageRequestDTO.getKeyword() != null && !pageRequestDTO.getKeyword().isEmpty()) {
            query.where(post.title.containsIgnoreCase(pageRequestDTO.getKeyword())
                    .or(post.authorName.containsIgnoreCase(pageRequestDTO.getKeyword())));
        }
        // 페이징 실제 처리
        Objects.requireNonNull(getQuerydsl()).applyPagination(pageable,query);

        List<Post> postList = query.fetch();
        long total = query.fetchCount();
        log.info("Post List : {}", postList);

        // Entity -> DTO 변환
        List<PostDTO> dtoList = postList.stream().map(p -> PostDTO.builder()
                .id(p.getId())
                .title(p.getTitle())
                .content(p.getContent())
                .authorName(p.getAuthorName())
                .authorEmail(p.getAuthorEmail())
                .authorRole(p.getAuthorRole())
                .createdAt(p.getCreatedAt().toLocalDate().toString())
                .updatedAt(p.getUpdatedAt().toLocalDate().toString())
                .build()).toList();

        return PageResponseDTO.<PostDTO>withAll()
                .dtoList(dtoList)
                .total(total)
                .pageRequestDTO(pageRequestDTO)
                .build();
    }
}
