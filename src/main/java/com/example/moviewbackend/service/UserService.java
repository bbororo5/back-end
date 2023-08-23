package com.example.moviewbackend.service;

import com.example.moviewbackend.dto.CommonResponseDto;
import com.example.moviewbackend.dto.SignupRequestDto;
import com.example.moviewbackend.entity.User;
import com.example.moviewbackend.entity.UserRoleEnum;
import com.example.moviewbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    // ADMIN_TOKEN
    private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    public ResponseEntity<CommonResponseDto> signup(SignupRequestDto requestDto) {
        String email = requestDto.getEmail();
        String password = passwordEncoder.encode(requestDto.getPassword());
        String nickname = requestDto.getNickname();

        //이메일 중복 확인
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("이미 존재하는 회원입니다.");
        }
        //닉네임 중복 확인
        if(userRepository.existsByNickname(nickname)) {
            throw new IllegalArgumentException("이미 존재하는 닉네임입니다.");
        }

        // 사용자 ROLE 확인
        UserRoleEnum role = UserRoleEnum.USER;
        if (requestDto.isAdmin()) {
            if (!ADMIN_TOKEN.equals(requestDto.getAdminToken())) {
                throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능합니다.");
            }
            role = UserRoleEnum.ADMIN;
        }

        User user = new User(email, password, nickname, role);
        userRepository.save(user);

        CommonResponseDto responseDto = CommonResponseDto.builder()
                .status(HttpStatus.CREATED)
                .message("회원가입 성공")
                .build();

        return ResponseEntity.status(responseDto.getStatus()).body(responseDto);
    }

    public ResponseEntity<CommonResponseDto> withdrawal(User user) {
        userRepository.delete(user);

        CommonResponseDto responseDto = CommonResponseDto.builder()
                .status(HttpStatus.OK)
                .message("회원탈퇴 성공")
                .build();

        return ResponseEntity.status(responseDto.getStatus()).body(responseDto);
    }


    public ResponseEntity<CommonResponseDto> isEmailUnique(String email) {
        boolean check = userRepository.existsByEmail(email);

        if (check) {
            throw new IllegalArgumentException("중복되는 이메일입니다.");
        }

        CommonResponseDto responseDto = CommonResponseDto.builder()
                .status(HttpStatus.OK)
                .message("사용 가능한 이메일입니다.")
                .build();

        return ResponseEntity.status(responseDto.getStatus()).body(responseDto);
    }

    public ResponseEntity<CommonResponseDto> isNicknameUnique(String nickname) {
        boolean check = userRepository.existsByNickname(nickname);

        if (check) {
            throw new IllegalArgumentException("중복되는 낙네임입니다.");
        }

        CommonResponseDto responseDto = CommonResponseDto.builder()
                .status(HttpStatus.OK)
                .message("사용 가능한 닉네임입니다.")
                .build();
        return ResponseEntity.status(responseDto.getStatus()).body(responseDto);
    }

}