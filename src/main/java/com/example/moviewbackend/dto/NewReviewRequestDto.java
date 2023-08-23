package com.example.moviewbackend.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class NewReviewRequestDto {
    @NotNull
    private Long movieId;
    @NotBlank
    private String content;
    @NotNull
    @DecimalMin(value = "0.5")
    @DecimalMax(value = "5.0")
    private float star;
}
