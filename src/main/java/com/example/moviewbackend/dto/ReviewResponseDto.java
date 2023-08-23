package com.example.moviewbackend.dto;

import com.example.moviewbackend.entity.Review;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ReviewResponseDto {
    private String movieTitle;
    private Long reviewId;
    private String nickname;
    private String content;
    private int likesCount;
    private int commentsCount;
    private float star;

    public ReviewResponseDto(Review review) {
        this.movieTitle = review.getMovie().getTitle();
        this.reviewId = review.getId();
        this.nickname = review.getUser().getNickname();
        this.content = review.getContent();
        this.likesCount = review.getLikesCount();
        this.commentsCount = review.getCommentsCount();
        this.star = review.getStar();
    }
}
