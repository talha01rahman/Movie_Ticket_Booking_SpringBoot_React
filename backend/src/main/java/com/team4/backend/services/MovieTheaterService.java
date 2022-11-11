package com.team4.backend.services;

import com.team4.backend.model.Movie;
import com.team4.backend.model.MovieTheater;
import com.team4.backend.model.Ticket;
import com.team4.backend.repository.MovieRepository;
import com.team4.backend.repository.MovieTheaterRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
public class MovieTheaterService {

    @Autowired
    private MovieTheaterRepository movieTheaterRepository;

    @Autowired
    private MovieRepository movieRepository;

    public List<MovieTheater> getClients() {
        List<MovieTheater> movieTheaterList = movieTheaterRepository.findAll();
        for (int i = 0; i < movieTheaterList.size(); i++) {
            List<Movie> movieList = movieTheaterList.get(i).getMovieList();
            List<Movie> simplifiedMovieList = new ArrayList<>();
            for (int j = 0; j < 3; j++) {
                simplifiedMovieList.add(j, new Movie(movieList.get(j).getName(), movieList.get(j).getImage()));
            }
            movieTheaterList.get(i).setMovieList(simplifiedMovieList);
        }
        return movieTheaterList;
    }

    public List<Movie> getMovieList(@PathVariable Long id) {
        List<MovieTheater> movieTheaterList = movieTheaterRepository.findAll();
        for (int i = 0; i < movieTheaterList.size(); i++) {
            if (Objects.equals(movieTheaterList.get(i).getId(), id)) {
                List<Movie> movieList = movieTheaterList.get(i).getMovieList();
                for (int j = 0; j < movieList.size(); j++) {
                    List<Ticket> ticketList = movieList.get(j).getTickets();
                    List<Ticket> simplifiedTicketList = new ArrayList<>();
                    simplifiedTicketList.add(0, new Ticket(ticketList.get(0).getTicketTime()));
                    simplifiedTicketList.add(1, new Ticket(ticketList.get(ticketList.size() - 1).getTicketTime()));
                    movieList.get(j).setTickets(simplifiedTicketList);
                }
                return movieList;
            }
        }
        return Collections.emptyList();
    }
}
