package com.example.moviewbackend.service;

import com.example.moviewbackend.dto.MovieResponseDto;
import com.example.moviewbackend.dto.Top5MovieResponseDto;
import com.example.moviewbackend.entity.Movie;
import com.example.moviewbackend.entity.Review;
import com.example.moviewbackend.repository.MovieRepository;
import com.example.moviewbackend.repository.ReviewRepository;
import com.example.moviewbackend.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MovieService {
    private final MovieRepository movieRepository;
    private final ReviewRepository reviewRepository;

    public MovieResponseDto getMovie(Optional<UserDetailsImpl> userDetails, Long id) {
        if (userDetails.isPresent()) { // 현재 접속한 사용자가 있고
            Optional<Review> review = reviewRepository.findByMovieIdAndUserId(id, userDetails.get().getUser().getId());
            if (review.isPresent()) { // 해당 영화에 대한 리뷰가 있다면
                Float star = review.get().getStar();
                return new MovieResponseDto(findMovie(id), review.get().getId(), star); // id, 별점 포함시키기
            }
        }
        return new MovieResponseDto(findMovie(id));
    }

    public List<MovieResponseDto> getMoviesByGenre(String genre) {
        return movieRepository.findAllByGenre(genre)
                .stream()
                .map(MovieResponseDto::new)
                .toList();
    }

    public List<Top5MovieResponseDto> getTop5Movies() {
        return movieRepository.findTop5ByOrderByRateDesc()
                .stream()
                .map(Top5MovieResponseDto::new)
                .toList();

    }

    protected Movie findMovie(Long id) {
        return movieRepository.findById(id).orElseThrow(() ->
                new NullPointerException("선택한 영화는 존재하지 않습니다.")
        );
    }
}
