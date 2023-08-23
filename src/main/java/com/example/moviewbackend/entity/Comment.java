package com.example.moviewbackend.entity;

import com.example.moviewbackend.dto.CommentRequestDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "comments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends Timestamped {
    @Id
    @Column(name = "comment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "review_id")
    private Review review;

    public Comment(User user, Review review, CommentRequestDto requestDto) {
        this.user = user;
        this.review = review;
        this.content = requestDto.getContent();
    }

    public String getNickname() {
        return user.getNickname();
    }

    public void update(CommentRequestDto requestDto) {
        this.content = requestDto.getContent();
    }
}
