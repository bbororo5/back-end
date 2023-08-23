package com.example.moviewbackend.controller;

import com.example.moviewbackend.dto.CommentRequestDto;
import com.example.moviewbackend.dto.CommentResponseDto;
import com.example.moviewbackend.dto.CommonResponseDto;
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
    public ResponseEntity<CommentResponseDto> createComment(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                            @PathVariable Long reviewId,
                                                            @Valid @RequestBody CommentRequestDto requestDto) {
        return commentService.createComment(userDetails.getUser(), reviewId, requestDto);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResponseDto> updateComment(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                            @PathVariable Long reviewId,
                                                            @PathVariable Long commentId,
                                                            @Valid @RequestBody CommentRequestDto requestDto) {
        return commentService.updateComment(userDetails.getUser(), reviewId, commentId, requestDto);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<CommonResponseDto> deleteComment(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                           @PathVariable Long reviewId,
                                                           @PathVariable Long commentId) {
        return commentService.deleteComment(userDetails.getUser(), reviewId, commentId);
    }

    @GetMapping
    public Page<CommentResponseDto> getComments(@PathVariable Long reviewId,
                                                @RequestParam Long lastCommentId,
                                                @RequestParam int size) {
        return commentService.getComments(reviewId, lastCommentId, size);
    }
}

