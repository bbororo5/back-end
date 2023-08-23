package com.example.moviewbackend.repository;

import com.example.moviewbackend.entity.Like;
import com.example.moviewbackend.entity.Review;
import com.example.moviewbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByUserAndReview(User user, Review review);
    boolean existsByUserAndReview(User user, Review review);

}
