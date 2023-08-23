package com.example.moviewbackend.service;

import com.example.moviewbackend.dto.CommentDto;
import com.example.moviewbackend.dto.CommentRequestDto;
import com.example.moviewbackend.dto.CommentResponseDto;
import com.example.moviewbackend.dto.CommonResponseDto;
import com.example.moviewbackend.entity.Comment;
import com.example.moviewbackend.entity.Review;
import com.example.moviewbackend.entity.User;
import com.example.moviewbackend.exception.CustomResponseException;
import com.example.moviewbackend.repository.CommentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final ReviewService reviewService;

    @Transactional
    public ResponseEntity<CommentResponseDto> createComment(User user, Long reviewId, CommentRequestDto requestDto) {
        // 리뷰 가져오기
        Review review = reviewService.findReview(reviewId);

        // 리뷰 생성
        Comment comment = new Comment(user, review, requestDto);

        // DB에 저장
        commentRepository.save(comment);
        review.updateCommentsCount(true);

        CommentDto commentDto = new CommentDto(comment.getId(), comment.getNickname(), comment.getContent() );

        return ResponseEntity.status(HttpStatus.CREATED).body(new CommentResponseDto(reviewId, commentDto));
    }

    @Transactional
    public ResponseEntity<CommentResponseDto> updateComment(User user, Long reviewId, Long commentId, CommentRequestDto requestDto) {
        Comment comment = findComment(commentId);

        // 작성자 맞는지 확인
        if (!comment.getUser().getId().equals(user.getId())) {
            throw new CustomResponseException(HttpStatus.FORBIDDEN, "작성자만 수정할 수 있습니다.");
        }

        comment.update(requestDto);
        CommentDto commentDto = new CommentDto(comment.getId(), comment.getNickname(), comment.getContent());

        return ResponseEntity.status(HttpStatus.OK).body(new CommentResponseDto(reviewId, commentDto));
    }

    @Transactional
    public ResponseEntity<CommonResponseDto> deleteComment(User user, Long reviewId, Long commentId) {
        Comment comment = findComment(commentId);

        // 작성자 맞는지 확인
        if (!comment.getUser().getId().equals(user.getId())) {
            throw new CustomResponseException(HttpStatus.FORBIDDEN, "작성자만 삭제할 수 있습니다.");
        }

        commentRepository.deleteById(commentId);
        Review review = reviewService.findReview(reviewId);
        review.updateCommentsCount(false);

        CommonResponseDto responseDto = CommonResponseDto.builder()
                .status(HttpStatus.OK)
                .message("댓글 삭제 성공")
                .build();
        return ResponseEntity.ok().body(responseDto);
    }

    public Page<CommentResponseDto> getComments(Long reviewId, Long lastCommentId, int size) {
        Pageable pageable = PageRequest.of(0, size); // no-offset 방식: page를 0으로 고정
        Page<Comment> commentsPage = commentRepository.findByReviewIdAndIdGreaterThan(reviewId, lastCommentId, pageable);

        return commentsPage.map(comment -> new CommentResponseDto(reviewId, new CommentDto(comment.getId(), comment.getNickname(), comment.getContent())));
    }

    protected Comment findComment(Long id) {
        return commentRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("해당하는 댓글이 존재하지 않습니다.")
        );
    }

}
