package com.example.moviewbackend.entity;

import com.example.moviewbackend.dto.NewReviewRequestDto;
import com.example.moviewbackend.dto.ReviewRequestDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "reviews")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends Timestamped {
    @Id
    @Column(name = "review_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private Float star;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @OneToMany(mappedBy = "review", cascade = CascadeType.REMOVE)
    private List<Like> likes = new ArrayList<>();

    @OneToMany(mappedBy = "review", cascade = CascadeType.REMOVE)
    private List<Comment> Comments = new ArrayList<>();

    @Column(nullable = false)
    @ColumnDefault("0")
    private Integer likesCount = 0;

    public Review(NewReviewRequestDto requestDto, User user, Movie movie) {
        super();
        this.content = requestDto.getContent();
        this.star = requestDto.getStar();
        this.user = user;
        this.movie = movie;
    }

    public void update(ReviewRequestDto requestDto) {
        this.content = requestDto.getContent();
        this.star = requestDto.getStar();
    }

    public void updateLike(boolean add) {
        if (add) {
            this.likesCount++;
        } else {
            this.likesCount--;
        }
    }
}
