package com.example.moviewbackend.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class CheckRequestDto {
    @Pattern(regexp = "^[a-z0-9ㄱ-ㅎ가-힣]{4,10}$",
            message = "최소 4자 이상, 10자 이하이며 알파벳 소문자 혹은 한글, 숫자(0~9) 로 구성되어야 합니다.")
    private String nickname;

    @Pattern(regexp = "^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$")
    private String email;
}
