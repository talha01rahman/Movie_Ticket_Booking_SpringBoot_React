package com.team4.backend.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "movie_table")
@JsonInclude(JsonInclude.Include.ALWAYS)
public class Movie implements Serializable {

    private @Id
    @GeneratedValue Long id;
    private Long identifier;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movieTheater")
    private MovieTheater movieTheater;
    private String name;
    private int duration;
    private String language;
    private String movieType;
    private String image;
    private String description;
    @OneToMany(targetEntity = Ticket.class, mappedBy = "movie", fetch = FetchType.EAGER)
    private List<Ticket> tickets;

    public Movie(Long identifier, MovieTheater movieTheater, String name, int duration, String language, String movieType, String image, String description) {
        this.identifier = identifier;
        this.movieTheater = movieTheater;
        this.name = name;
        this.duration = duration;
        this.language = language;
        this.movieType = movieType;
        this.image = image;
        this.description = description;
    }

    public Movie(Movie sample) {
        this.name = sample.name;
        this.duration = sample.duration;
        this.language = sample.language;
        this.movieType = sample.movieType;
        this.image = sample.image;
        this.description = sample.description;
    }

    public Movie(String name, String image) {
        this.name = name;
        this.image = image;
    }

    public Movie() {
    }

    public Long getMovieTheaterId() {
        if (movieTheater != null) {
            return movieTheater.getId();
        }
        return (long) -1;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setMovieTheater(MovieTheater movieTheater) {
        this.movieTheater = movieTheater;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getMovieType() {
        return movieType;
    }

    public void setMovieType(String movieType) {
        this.movieType = movieType;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getIdentifier() {
        return identifier;
    }

    public void setIdentifier(Long identifier) {
        this.identifier = identifier;
    }

}
