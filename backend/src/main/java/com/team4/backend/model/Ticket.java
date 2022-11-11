package com.team4.backend.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "ticket_table")
@JsonInclude(JsonInclude.Include.ALWAYS)
public class Ticket implements Serializable {
    private @Id
    @GeneratedValue Long id;
    private String seatInfo;
    private boolean bookedOrNot = false;
    private String ticketTime;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id")
    private Movie movie;
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "user_id", nullable = true)
    private CustomizedUser customizedUser;

    public Ticket() {
    }
    public Ticket(String ticketTime) {
        this.ticketTime = ticketTime;
    }

    public String getTicketTime() {
        return ticketTime;
    }

    public void setTicketTime(String ticketTime) {
        this.ticketTime = ticketTime;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isBookedOrNot() {
        return bookedOrNot;
    }

    public void setBookedOrNot(boolean bookedOrNot) {
        this.bookedOrNot = bookedOrNot;
    }

    public String getSeatInfo() {
        return seatInfo;
    }

    public void setSeatInfo(String seatInfo) {
        this.seatInfo = seatInfo;
    }

    public void setCustomizedUser(CustomizedUser customizedUser) {
        this.customizedUser = customizedUser;
    }

    public Long getMovieId() {
        if (movie != null) {
            return movie.getId();
        }
        return (long) -1;
    }

    public Long getCustomizedUserId() {
        if (customizedUser != null) {
            return customizedUser.getId();
        }
        return (long) -1;
    }

}
