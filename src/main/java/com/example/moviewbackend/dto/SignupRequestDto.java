package com.example.moviewbackend.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class SignupRequestDto {
    @Pattern(regexp = "^[a-z0-9가-힣]{4,10}$",
            message = "닉네임은 최소 4자 이상, 10자 이하이며 알파벳 소문자 혹은 한글, 숫자(0~9) 로 구성되어야 합니다.")
    private String nickname;

    @Pattern(regexp = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$")
    private String email;

    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,15}$",
            message = "최소 8자 이상, 15자 이하이며 알파벳 대소문자(a~z, A~Z), 숫자(0~9), 특수문자 로 구성되어야 합니다.")
    private String password;

    private boolean admin = false;
    private String adminToken = "";
}
