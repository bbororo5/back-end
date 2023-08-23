package com.example.moviewbackend.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CommentResponseDto {

    private Long reviewId;
    private CommentDto comment;

    public CommentResponseDto(Long reviewId, CommentDto comment) {
        this.reviewId = reviewId;
        this.comment = comment;
    }


}