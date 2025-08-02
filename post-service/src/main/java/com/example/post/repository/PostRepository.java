package com.example.post.repository;

import com.example.post.entity.Post;
import com.example.post.repository.search.PostSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, PostSearch {
    @Query("select p, pi from Post p left join p.fileNames pi where pi.ord = 0")
    Page<Object[]> selectList(Pageable pageable);
}
