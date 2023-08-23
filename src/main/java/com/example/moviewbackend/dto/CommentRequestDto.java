package com.example.moviewbackend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CommentRequestDto {
    @NotNull
    private String content;
}
