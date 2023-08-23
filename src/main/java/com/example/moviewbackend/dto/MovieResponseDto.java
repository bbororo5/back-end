package com.example.moviewbackend.dto;

import com.example.moviewbackend.entity.Movie;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.time.LocalDate;

@Getter
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MovieResponseDto {
    private Long movieId;
    private String title;
    private LocalDate openDate;
    private String director;
    private String genre;
    private String thumbnail;
    private String still;
    private Float rate;
    private Long reviewId;
    private Float star;

    public MovieResponseDto(Movie movie) {
        this.movieId = movie.getId();
        this.title = movie.getTitle();
        this.openDate = movie.getOpenDate();
        this.director = movie.getDirector();
        this.genre = movie.getGenre();
        this.thumbnail = movie.getThumbnail();
        this.still = movie.getStill();
        this.rate = movie.getRate();
    }

    public MovieResponseDto(Movie movie, Long reviewId, Float star) {
        this.movieId = movie.getId();
        this.reviewId = reviewId;
        this.title = movie.getTitle();
        this.openDate = movie.getOpenDate();
        this.director = movie.getDirector();
        this.genre = movie.getGenre();
        this.thumbnail = movie.getThumbnail();
        this.still = movie.getStill();
        this.rate = movie.getRate();
        this.star = star;
    }
}
