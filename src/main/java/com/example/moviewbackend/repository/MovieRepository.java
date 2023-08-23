package com.example.moviewbackend.repository;

import com.example.moviewbackend.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    List<Movie> findAllByGenre(String genre);

    List<Movie> findTop5ByOrderByRateDesc();
}
