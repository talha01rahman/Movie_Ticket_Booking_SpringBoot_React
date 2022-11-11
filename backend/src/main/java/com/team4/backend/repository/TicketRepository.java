package com.team4.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.team4.backend.model.Ticket;

import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    Optional<Ticket> findTicketById(Long id);
}
