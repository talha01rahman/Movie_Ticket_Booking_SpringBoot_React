package com.team4.backend.controller;

import com.team4.backend.model.Ticket;
import com.team4.backend.services.TicketService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/app/{movieIdStr}/booking")
public class TicketController {
    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping("/{ticketIdStr}")
    public Map<String, List<Ticket>> getSeats(@PathVariable("movieIdStr") String movieIdStr, @PathVariable("ticketIdStr") String ticketIdStr) {
        return ticketService.getSeats(movieIdStr, ticketIdStr);
    }

    @GetMapping("/info/{ticketIdStr}")
    public Map<String, String> getMovieInfo(@PathVariable("movieIdStr") String movieIdStr, @PathVariable("ticketIdStr") String ticketIdStr) {
        return ticketService.getMovieInfo(movieIdStr, ticketIdStr);
    }

    @PostMapping("/update/{userid}")
    public ResponseEntity<?> reserveSeats(@PathVariable("movieIdStr") String movieIdStr, @PathVariable("userid") String userIdStr, @RequestBody String reservedTickets) {
        return ticketService.reserveSeats(movieIdStr, userIdStr, reservedTickets);
    }

}
