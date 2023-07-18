package com.example.moviewbackend.controller;

import com.example.moviewbackend.dto.MovieResponseDto;
import com.example.moviewbackend.dto.Top5MovieResponseDto;
import com.example.moviewbackend.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
public class MovieController {
    private final MovieService movieService;

//    /**
//     * 영화 전체 조회
//     * - 개봉일 기준 내림차순
//     * @return MovieResponseDto 리스트 반환
//     */
//    @GetMapping
//    public List<MovieResponseDto> getMovies() {
//        return movieService.getMovies();
//    }

    /**
     * 영화 상세 조회
     * @return id에 해당하는 MovieResponseDto 반환
     */
    @GetMapping("/{id}")
    public MovieResponseDto getMovie(@PathVariable Long id) {
        return movieService.getMovie(id);
    }

    /**
     * 장르별 영화 조회
     * @param genre 영화 장르
     * @return MovieResponseDto 리스트 반환
     */
    @GetMapping
    public List<MovieResponseDto> getMoviesByGenre(@RequestParam String genre) {
        return movieService.getMoviesByGenre(genre);
    }

    @GetMapping("/top5")
    public List<Top5MovieResponseDto> getTop5Movies() {
        return movieService.getTop5Movies();
    }
}