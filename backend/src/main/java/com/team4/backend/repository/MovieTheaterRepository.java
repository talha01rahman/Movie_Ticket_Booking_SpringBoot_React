package com.team4.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.team4.backend.model.MovieTheater;

import java.util.Optional;

public interface MovieTheaterRepository extends JpaRepository<MovieTheater, Long> {
    Optional<MovieTheater> findById(Long id);
}
