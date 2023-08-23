package com.example.moviewbackend.repository;

import com.example.moviewbackend.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findById(Long commentId);
    Page<Comment> findByReviewIdAndIdGreaterThan(Long reviewId, Long lastCommentId, Pageable pageable);
}
