package com.team4.backend;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.team4.backend.model.Movie;
import com.team4.backend.model.MovieTheater;
import com.team4.backend.model.Ticket;
import com.team4.backend.repository.MovieRepository;
import com.team4.backend.repository.MovieTheaterRepository;
import com.team4.backend.repository.TicketRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;
import java.util.Random;


@Component
public class DatabaseLoader implements CommandLineRunner {
    private final MovieTheaterRepository movieTheaterRepository;
    private final MovieRepository movieRepository;
    private final TicketRepository ticketRepository;

    @Autowired
    public DatabaseLoader(MovieTheaterRepository movieTheaterRepository, MovieRepository movieRepository, TicketRepository ticketRepository) {
        this.movieTheaterRepository = movieTheaterRepository;
        this.movieRepository = movieRepository;
        this.ticketRepository = ticketRepository;
    }

    @Override
    public void run(String... strings) throws Exception {
        File file = ResourceUtils.getFile("classpath:data.json");
        String content = new String(Files.readAllBytes(file.toPath()));


        Gson gson = new Gson();
        JsonObject mJsonObject = JsonParser.parseString(content).getAsJsonObject();
        Type typeMovieTheater = new TypeToken<List<MovieTheater>>() {
        }.getType();
        String movieTheaterStr = mJsonObject.get("MovieTheater").toString();
        List<MovieTheater> movieTheaterList = gson.fromJson(movieTheaterStr, typeMovieTheater);

        Type typeMovie = new TypeToken<List<Movie>>() {
        }.getType();
        String movieStr = mJsonObject.get("Movie").toString();
        List<Movie> movieList = gson.fromJson(movieStr, typeMovie);

        Type typeTime = new TypeToken<List<String>>() {
        }.getType();
        List<String> morningTimeList = gson.fromJson(mJsonObject.get("morningMovieTime").toString(), typeTime);
        List<String> afternoonTimeList = gson.fromJson(mJsonObject.get("afternoonMovieTime").toString(), typeTime);

        Random rand = new Random();
        for (int i = 0; i < movieTheaterList.size(); i++) {
            MovieTheater movieTheater = movieTheaterList.get(i);
            this.movieTheaterRepository.save(movieTheater);

            int randomMovieNum = rand.nextInt((5 - 3) + 1) + 4;
            Collections.shuffle(movieList);
            for (int j = 0; j < randomMovieNum; j++) {
                Movie movie = new Movie(movieList.get(j));
                movie.setMovieTheater(movieTheater);
                this.movieRepository.save(movie);

                Collections.shuffle(morningTimeList);
                for (int k = 0; k < 30; k++) {
                    Ticket ticket = new Ticket();
                    ticket.setMovie(movie);
                    ticket.setTicketTime(morningTimeList.get(0));
                    this.ticketRepository.save(ticket);
                }

                Collections.shuffle(afternoonTimeList);
                for (int k = 0; k < 30; k++) {
                    Ticket ticket = new Ticket();
                    ticket.setMovie(movie);
                    ticket.setTicketTime(afternoonTimeList.get(0));
                    this.ticketRepository.save(ticket);
                }
            }
        }
    }
}
