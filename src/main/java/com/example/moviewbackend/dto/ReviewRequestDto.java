package com.example.moviewbackend.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ReviewRequestDto {
    @NotBlank
    private String content;
    @NotNull
    @DecimalMin(value = "0.5")
    @DecimalMax(value = "10.0")
    private float star;
}
