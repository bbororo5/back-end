package com.example.moviewbackend.service;

import com.example.moviewbackend.dto.SignupRequestDto;
import com.example.moviewbackend.entity.User;
import com.example.moviewbackend.entity.UserRoleEnum;
import com.example.moviewbackend.jwt.JwtUtil;
import com.example.moviewbackend.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final JwtUtil jwtUtil;

    public void signup(SignupRequestDto requestDto) {
        String email = requestDto.getEmail();
        String password = passwordEncoder.encode(requestDto.getPassword());
        String nickname = requestDto.getNickname();
        UserRoleEnum role = requestDto.getRole();

        //이메일 중복 확인
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 회원입니다.");
        }
        //닉네임 중복 확인
        if(userRepository.findByNickname(nickname).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 닉네임입니다.");
        }

        User user = new User(email, password, nickname, role);
        userRepository.save(user);
    }

    public void withdrawal(Long id, HttpServletRequest request) {
        String token = jwtUtil.getTokenFromRequest(request);
        Claims claims = jwtUtil.getUserInfoFromToken(token);
        String email = claims.getSubject();
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new IllegalArgumentException("등록된 사용자가 아닙니다.")
        );

        if(!user.getId().equals(id)) {
            throw new IllegalStateException("You can't delete another user's account!");
        }
        userRepository.delete(user);

        //댓글, 리뷰, 좋아요 삭제 필요


    }
}