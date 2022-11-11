package com.team4.backend.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "movie_theater_table")
@JsonInclude(JsonInclude.Include.ALWAYS)
public class MovieTheater implements Serializable {
    private @Id
    @GeneratedValue Long id;

    private Long identifier;
    private String name;
    private String location;
    private String city;
    private String description;
    @OneToMany(targetEntity = Movie.class, mappedBy = "movieTheater", fetch = FetchType.EAGER)
    private List<Movie> movieList;

    public MovieTheater() {
    }

    public MovieTheater(Long identifier, String name, String location, String city, String description) {
        this.identifier = identifier;
        this.name = name;
        this.location = location;
        this.city = city;
        this.description = description;
    }

    public Long getIdentifier() {
        return identifier;
    }

    public void setIdentifier(Long identifier) {
        this.identifier = identifier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MovieTheater movieTheater = (MovieTheater) o;
        return Objects.equals(id, movieTheater.id) &&
                Objects.equals(name, movieTheater.name) &&
                Objects.equals(location, movieTheater.location) &&
                Objects.equals(city, movieTheater.city) &&
                Objects.equals(description, movieTheater.description);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, name, location, city, description);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "MovieTheater{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", city='" + city + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    public List<Movie> getMovieList() {
        return movieList;
    }

    public void setMovieList(List<Movie> movieList) {
        this.movieList = movieList;
    }
}
