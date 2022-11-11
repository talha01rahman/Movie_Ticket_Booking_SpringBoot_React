package com.team4.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.team4.backend.model.CustomizedUser;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<CustomizedUser, Long> {
    @Query(value = "SELECT * FROM users u where u.email = ?1 and u.password = ?2 ", nativeQuery = true)
    Optional<CustomizedUser> login(String email, String password);

    Optional<CustomizedUser> findByToken(String token);

    Optional<CustomizedUser> findById(Long id);

}