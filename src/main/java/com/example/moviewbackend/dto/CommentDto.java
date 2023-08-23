package com.example.moviewbackend.dto;

import lombok.Getter;


@Getter
public class CommentDto {

    private Long id;
    private String nickname;
    private String content;

    public CommentDto() {
    }

    public CommentDto(Long id, String nickname, String content) {
        this.id = id;
        this.nickname = nickname;
        this.content = content;
    }
}
