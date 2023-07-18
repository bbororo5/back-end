package com.example.moviewbackend.controller;

import com.example.moviewbackend.dto.ApiResponseDto;
import com.example.moviewbackend.dto.CommentRequestDto;
import com.example.moviewbackend.dto.CommentResponseDto;
import com.example.moviewbackend.security.UserDetailsImpl;
import com.example.moviewbackend.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews/{reviewId}/comments")
@RequiredArgsConstructor
public class CommentController {


    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentResponseDto> createComment(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long reviewId, @Valid @RequestBody CommentRequestDto requestDto) {
        return commentService.createComment(userDetails.getUser(), reviewId, requestDto);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResponseDto> updateComment(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long reviewId, @PathVariable Long commentId) {
        return commentService.updateComment(userDetails.getUser(), reviewId, commentId);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<ApiResponseDto> deleteComment(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long reviewId, @PathVariable Long commentId) {
        commentService.deleteComment(userDetails.getUser(), commentId);
        return ResponseEntity.ok().body(new ApiResponseDto("댓글이 삭제 성공"));
    }


    public Page<CommentResponseDto> getAllComments(@RequestParam Long reviewId, @RequestParam Long lastCommentId, @RequestParam int size) {
        return commentService.getAllComments(reviewId, lastCommentId, size);
    }
}

