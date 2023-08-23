package com.example.moviewbackend.dto;

import com.example.moviewbackend.entity.Movie;
import com.example.moviewbackend.entity.Review;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class DetailReviewResponseDto {
    private Long reviewId;
    private String author;
    private String content;
    private int likesCount;
    private int commentsCount;
    private float star;
    private Boolean likeByUser; // 현재 접속자가 좋아요 누른 여부
    private Boolean isAuthor; // 현재 접속자가 작성자인지 여부
    private MovieResponseDto movie;

    public DetailReviewResponseDto(Review review, boolean likeByUser, boolean isAuthor) {
        this.reviewId = review.getId();
        this.author = review.getUser().getNickname();
        this.content = review.getContent();
        this.likesCount = review.getLikesCount();
        this.commentsCount = review.getCommentsCount();
        this.star = review.getStar();

        this.likeByUser = likeByUser;
        this.isAuthor = isAuthor;

        Movie movie = review.getMovie();
        this.movie = MovieResponseDto.builder()
                .movieId(movie.getId())
                .title(movie.getTitle())
                .openDate(movie.getOpenDate())
                .director(movie.getDirector())
                .thumbnail(movie.getThumbnail())
                .build();
    }
}
