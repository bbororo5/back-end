package com.example.moviewbackend.controller;


import com.example.moviewbackend.dto.CheckRequestDto;
import com.example.moviewbackend.dto.CommonResponseDto;
import com.example.moviewbackend.dto.SignupRequestDto;
import com.example.moviewbackend.security.UserDetailsImpl;
import com.example.moviewbackend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<CommonResponseDto> signUp(@Valid @RequestBody SignupRequestDto requestDto) {
        return userService.signup(requestDto);
    }

    @DeleteMapping
    public ResponseEntity<CommonResponseDto> withdrawal(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userService.withdrawal(userDetails.getUser());
    }

    @PostMapping("/check/email")
    public ResponseEntity<CommonResponseDto> checkEmail(@RequestBody CheckRequestDto requestDto) {
        return userService.isEmailUnique(requestDto.getEmail());


    }

    @PostMapping("/check/nickname")
    public ResponseEntity<CommonResponseDto> checkNickname(@RequestBody CheckRequestDto requestDto) {
        return userService.isNicknameUnique(requestDto.getNickname());
    }

}