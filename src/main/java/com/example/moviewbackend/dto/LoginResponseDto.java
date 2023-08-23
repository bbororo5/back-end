package com.example.moviewbackend.dto;

import lombok.Getter;

@Getter
public class LoginResponseDto extends CommonResponseDto {
    private String nickname;

    public LoginResponseDto(String nickname, String message) {
        super(message);
        this.nickname = nickname;
    }
}
