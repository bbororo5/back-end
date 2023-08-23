package com.example.moviewbackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "movies")
public class Movie {
    @Id
    @Column(name = "movie_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private LocalDate openDate;

    @Column(nullable = false)
    private String director;

    @Column(nullable = false)
    private String genre;

    @Column(nullable = false)
    private String thumbnail;

    @Column(nullable = false)
    private String still;

    @Column(nullable = false)
    @ColumnDefault("0.0")
    private Float rate; // 평균 별점

    @OneToMany(mappedBy = "movie", cascade = CascadeType.REMOVE)
    private List<Review> reviews = new ArrayList<>();

    public void updateStar() {
        this.rate = (float) reviews.stream()
                .mapToDouble(Review::getStar)
                .sum()/ reviews.size();
    }

    public void deleteStar(Float star){
        if (this.reviews.size() == 1) {
            this.rate = 0F;
        } else {
            this.rate = (this.rate * this.reviews.size() - star)/(this.reviews.size()-1);
        }
    }
}
