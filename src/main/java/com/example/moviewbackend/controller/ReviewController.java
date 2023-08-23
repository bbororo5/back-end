package com.example.moviewbackend.controller;

import com.example.moviewbackend.dto.*;
import com.example.moviewbackend.security.UserDetailsImpl;
import com.example.moviewbackend.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewResponseDto> createReview(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                          @RequestBody @Valid NewReviewRequestDto requestDto) {
        return reviewService.createReview(userDetails.getUser(), requestDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReviewResponseDto> updateReview(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                          @PathVariable Long id,
                                                          @RequestBody @Valid ReviewRequestDto requestDto) {
        return reviewService.updateReview(userDetails.getUser(), id, requestDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetailReviewResponseDto> getReview(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                             @PathVariable Long id) {
        return reviewService.getReview(Optional.ofNullable(userDetails), id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponseDto> deleteReview(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                          @PathVariable Long id) {
        return reviewService.deleteReview(userDetails.getUser(), id);
    }

    @PostMapping("/{id}/likes")
    public ResponseEntity<ReviewResponseDto> like(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                  @PathVariable Long id) {
        return reviewService.like(userDetails.getUser(), id);
    }

    @DeleteMapping("/{id}/likes")
    public ResponseEntity<ReviewResponseDto> dislike(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                     @PathVariable Long id) {
        return reviewService.dislike(userDetails.getUser(), id);
    }

    @GetMapping
    public Page<ReviewResponseDto> getReviews(@RequestParam Long movieId,
                                              @RequestParam Long lastReviewId,
                                              @RequestParam int size) {
        return reviewService.getReviews(movieId, lastReviewId, size);
    }
}
