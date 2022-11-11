package com.team4.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.team4.backend.model.Movie;

import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    Optional<Movie> findMovieById(Long id);
}
