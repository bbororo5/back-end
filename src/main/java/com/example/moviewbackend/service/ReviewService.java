package com.example.moviewbackend.service;

import com.example.moviewbackend.dto.CommonResponseDto;
import com.example.moviewbackend.entity.Like;
import com.example.moviewbackend.exception.CustomResponseException;
import com.example.moviewbackend.dto.ReviewRequestDto;
import com.example.moviewbackend.dto.ReviewResponseDto;
import com.example.moviewbackend.entity.Movie;
import com.example.moviewbackend.entity.Review;
import com.example.moviewbackend.entity.User;
import com.example.moviewbackend.repository.LikeRepository;
import com.example.moviewbackend.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final UserService userService;

    public ResponseEntity<ReviewResponseDto> createReview(Long movieId, ReviewRequestDto requestDto) {
        // 영화 가져오기
        Movie movie = movieService.findMovie(movieId);

        // 사용자 가져오기 -> 나중에 userdetail로 바꾸기
        User user = userService.findUser(requestDto.getUser_id());

        // 리뷰 생성
        Review review = new Review(requestDto, user, movie);

        // DB에 저장
        reviewRepository.save(review);

        ReviewResponseDto responseDto = ReviewResponseDto.builder()
                .movieTitle(movie.getTitle())
                .reviewId(review.getId())
                .nickname(user.getNickname())
                .content(review.getContent())
                .star(review.getStar())
                .likesCount(review.getLikes().size())
                .build();

        return ResponseEntity.status(201).body(responseDto);
    }

    @Transactional
    public ResponseEntity<ReviewResponseDto> updateReview(Long movieId, Long id, ReviewRequestDto requestDto) {
        // 리뷰 가져오기
        Review review = findReview(movieId, id);

        // 요청한 사용자 가져오기 -> 나중에 userdetail로 바꾸기
        // 작성자 맞는지 확인
        if (!review.getUser().getId().equals(requestDto.getUser_id())) { // 임시로 아이디로 확인
            throw new CustomResponseException(HttpStatus.FORBIDDEN, "작성자만 수정할 수 있습니다.");
        }

        // 엔티티 업데이트
        review.update(requestDto);

        ReviewResponseDto responseDto = ReviewResponseDto.builder()
                .movieTitle(review.getMovie().getTitle())
                .reviewId(review.getId())
                .nickname(review.getUser().getNickname())
                .content(review.getContent())
                .star(review.getStar())
                .likesCount(review.getLikes().size())
                .build();

        return ResponseEntity.status(200).body(responseDto);
    }

    public ResponseEntity<ReviewResponseDto> getReview(Long movieId, Long id) {
        // 리뷰 가져오기
        Review review = findReview(movieId, id);
        ReviewResponseDto responseDto = ReviewResponseDto.builder()
                .movieTitle(review.getMovie().getTitle())
                .reviewId(review.getId())
                .nickname(review.getUser().getNickname())
                .content(review.getContent())
                .star(review.getStar())
                .likesCount(review.getLikes().size())
                .build();

        return ResponseEntity.status(200).body(responseDto);
    }

    public ResponseEntity<CommonResponseDto> deleteReview(Long movieId, Long id) {
        // 리뷰 가져오기
        Review review = findReview(movieId, id);

        // 요청한 사용자 가져오기 -> 나중에 userdetail로 바꾸기
        // 작성자 맞는지 확인
        if (!review.getUser().getId().equals(1L)) { // 임시로 아이디로 확인
            throw new CustomResponseException(HttpStatus.FORBIDDEN, "작성자만 삭제할 수 있습니다.");
        }

        reviewRepository.delete(review);

        CommonResponseDto responseDto = CommonResponseDto.builder(HttpStatus.OK, "리뷰 삭제 성공").build();
        return ResponseEntity.status(responseDto.getStatus()).body(responseDto);
    }

    public ResponseEntity<ReviewResponseDto> like(Long movieId, Long id) {
        // 사용자 가져오기 -> 나중에 userdetail로 바꾸기
        User user = userService.findUser(1L);
        // 리뷰 가져오기
        Review review = findReview(movieId, id);

        if (likeRepository.findByUserAndReview(user, review).isPresent()) {
            throw new IllegalArgumentException("이미 좋아요를 눌렀습니다.");
        }

        Like like = Like.builder()
                .user(user)
                .review(review)
                .build();

        likeRepository.save(like);

        ReviewResponseDto responseDto = ReviewResponseDto.builder()
                .movieTitle(review.getMovie().getTitle())
                .reviewId(review.getId())
                .nickname(review.getUser().getNickname())
                .content(review.getContent())
                .star(review.getStar())
                .likesCount(review.getLikes().size())
                .build();

        return ResponseEntity.ok(responseDto);
    }

    public ResponseEntity<ReviewResponseDto> dislike(Long movieId, Long id) {
        // 사용자 가져오기 -> 나중에 userdetail로 바꾸기
        User user = userService.findUser(1L);
        // 리뷰 가져오기
        Review review = findReview(movieId, id);
        // 좋아요 가져오기
        Like like = likeRepository.findByUserAndReview(user, review).orElseThrow(() ->
                new IllegalArgumentException("선택한 좋아요는 존재하지 않습니다.")
        );

        likeRepository.delete(like);

        ReviewResponseDto responseDto = ReviewResponseDto.builder()
                .movieTitle(review.getMovie().getTitle())
                .reviewId(review.getId())
                .nickname(review.getUser().getNickname())
                .content(review.getContent())
                .star(review.getStar())
                .likesCount(review.getLikes().size())
                .build();

        return ResponseEntity.ok(responseDto);
    }

    protected Review findReview(Long movieId, Long id) {
        return reviewRepository.findByMovieIdAndId(movieId, id).orElseThrow(() ->
                new IllegalArgumentException("선택한 리뷰가 존재하지 않습니다.")
        );
    }
}