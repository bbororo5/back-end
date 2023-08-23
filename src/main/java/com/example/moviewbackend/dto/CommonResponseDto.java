package com.example.moviewbackend.dto;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommonResponseDto {
    LocalDateTime timestamp;
    HttpStatus status;
    String message;
    String path;
    public CommonResponseDto(String message) {
        this.message = message;
    }
}
/*
{
    "timestamp": "2023-07-04T07:47:03.710+00:00",
    "status": 400,
    "message": "Bad Request",
    "path": "/api/posts/1/comment"
}
 */

