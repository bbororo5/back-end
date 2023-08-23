package com.example.moviewbackend.jwt;

import com.example.moviewbackend.dto.CommonResponseDto;
import com.example.moviewbackend.dto.LoginRequestDto;
import com.example.moviewbackend.dto.LoginResponseDto;
import com.example.moviewbackend.entity.UserRoleEnum;
import com.example.moviewbackend.security.UserDetailsImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Slf4j(topic = "로그인 및 JWT 생성")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
        setFilterProcessesUrl("/api/users/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info("로그인 시도");
        try {
            LoginRequestDto requestDto = new ObjectMapper().readValue(request.getInputStream(), LoginRequestDto.class);

            return getAuthenticationManager().authenticate( // authenticate method
                    new UsernamePasswordAuthenticationToken(
                            requestDto.getEmail(),
                            requestDto.getPassword(),
                            null
                    )
            );
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        log.info("로그인 성공 및 JWT 생성");
        UserDetailsImpl userDetails = ((UserDetailsImpl) authResult.getPrincipal());
        String username = userDetails.getUsername();
        UserRoleEnum role = userDetails.getUser().getRole();

        String token = jwtUtil.createToken(username, role);
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, token);

        response.setStatus(200);
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        LoginResponseDto responseDto = new LoginResponseDto(userDetails.getUser().getNickname(), "로그인 성공");

        String result = new ObjectMapper().writeValueAsString(responseDto);
        response.getWriter().write(result);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        log.info("로그인 실패");
        response.setStatus(401);
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        CommonResponseDto responseDto = CommonResponseDto.builder()
                .message("로그인 실패")
                .build();
        String result = new ObjectMapper().writeValueAsString(responseDto);
        response.getWriter().write(result);
    }
}

