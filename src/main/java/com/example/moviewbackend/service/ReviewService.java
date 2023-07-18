package com.example.moviewbackend.service;

import com.example.moviewbackend.dto.CommonResponseDto;
import com.example.moviewbackend.dto.NewReviewRequestDto;
import com.example.moviewbackend.dto.ReviewRequestDto;
import com.example.moviewbackend.dto.ReviewResponseDto;
import com.example.moviewbackend.entity.Like;
import com.example.moviewbackend.entity.Movie;
import com.example.moviewbackend.entity.Review;
import com.example.moviewbackend.entity.User;
import com.example.moviewbackend.exception.CustomResponseException;
import com.example.moviewbackend.repository.LikeRepository;
import com.example.moviewbackend.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final LikeRepository likeRepository;
    private final MovieService movieService;

    @Transactional
    public ResponseEntity<ReviewResponseDto> createReview(User user, NewReviewRequestDto requestDto) {
        // 영화 가져오기
        Movie movie = movieService.findMovie(requestDto.getMovieId());

        // 리뷰 생성
        Review review = new Review(requestDto, user, movie);

        // DB에 저장
        reviewRepository.save(review);

        // 별점 업데이트
        movie.updateStar();

        return ResponseEntity.status(201).body(new ReviewResponseDto(review));
    }

    @Transactional
    public ResponseEntity<ReviewResponseDto> updateReview(User user, Long id, ReviewRequestDto requestDto) {
        // 리뷰 가져오기
        Review review = findReview(id);

        // 작성자 맞는지 확인
        if (!review.getUser().getId().equals(user.getId())) {
            throw new CustomResponseException(HttpStatus.FORBIDDEN, "작성자만 수정할 수 있습니다.");
        }

        // 엔티티 업데이트
        review.update(requestDto);

        review.getMovie().updateStar();

      return ResponseEntity.status(200).body(new ReviewResponseDto(review));
    }

    public ResponseEntity<ReviewResponseDto> getReview(Long id) {
        // 리뷰 가져오기
        Review review = findReview(id);
      
        return ResponseEntity.status(200).body(new ReviewResponseDto(review));
    }

    public ResponseEntity<CommonResponseDto> deleteReview(User user, Long id) {
        // 리뷰 가져오기
        Review review = findReview(id);

        // 작성자 맞는지 확인
        if (!review.getUser().getId().equals(user.getId())) {
            throw new CustomResponseException(HttpStatus.FORBIDDEN, "작성자만 삭제할 수 있습니다.");
        }

        reviewRepository.delete(review);

        Movie movie = review.getMovie();
        // 별점 업데이트
        movie.updateStar();

        CommonResponseDto responseDto = CommonResponseDto.builder(HttpStatus.OK, "리뷰 삭제 성공").build();
        return ResponseEntity.status(responseDto.getStatus()).body(responseDto);
    }

    @Transactional
    public ResponseEntity<ReviewResponseDto> like(User user, Long id) {
        // 리뷰 가져오기
        Review review = findReview(id);

        // 사용자가 해당 리뷰에 좋아요를 이미 눌렀는지 확인
        if (likeRepository.findByUserAndReview(user, review).isPresent()) {
            throw new IllegalArgumentException("이미 좋아요를 눌렀습니다.");
        }

        Like like = Like.builder()
                .user(user)
                .review(review)
                .build();

        likeRepository.save(like);
        review.updateLike(true);

        return ResponseEntity.ok(new ReviewResponseDto(review));
    }

    @Transactional
    public ResponseEntity<ReviewResponseDto> dislike(User user, Long id) {
        // 리뷰 가져오기
        Review review = findReview(id);
        // 좋아요 가져오기
        Like like = likeRepository.findByUserAndReview(user, review).orElseThrow(() ->
                new IllegalArgumentException("선택한 좋아요는 존재하지 않습니다.")
        );

        likeRepository.delete(like);
        review.updateLike(false);

        return ResponseEntity.ok(new ReviewResponseDto(review));
    }

    public Page<ReviewResponseDto> getReviews(Long movieId, Long lastReviewId, int size) {
        Pageable pageable = PageRequest.of(0, size); // no-offset 방식: page를 0으로 고정
        // Long.MAX_VALUE=9223372036854775807
        Page<Review> reviewPage = reviewRepository.findByMovieIdAndIdLessThanOrderByIdDesc(movieId, lastReviewId, pageable);
        return reviewPage.map(ReviewResponseDto::new);
    }

    protected Review findReview(Long id) {
        return reviewRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("선택한 리뷰가 존재하지 않습니다.")
        );
    }
}