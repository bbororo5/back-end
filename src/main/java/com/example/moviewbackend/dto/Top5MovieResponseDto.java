package com.example.moviewbackend.dto;

import com.example.moviewbackend.entity.Movie;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

import java.util.Comparator;
import java.util.List;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Top5MovieResponseDto {
    private Long movieId;
    private String title;
    private String still;
    private float rate;
    private List<ReviewResponseDto> reviews;

    public Top5MovieResponseDto(Movie movie) {
        this.movieId = movie.getId();
        this.title = movie.getTitle();
        this.still = movie.getStill();
        this.rate = movie.getRate();
        this.reviews = movie.getReviews()
                .stream()
                .map(ReviewResponseDto::new)
                .sorted(Comparator.comparing(ReviewResponseDto::getLikesCount).reversed())
                .limit(3)
                .toList();
    }
}
