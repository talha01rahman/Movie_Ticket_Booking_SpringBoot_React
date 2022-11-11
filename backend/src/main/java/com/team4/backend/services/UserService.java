package com.team4.backend.services;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.team4.backend.exception.InvalidTicketInfoException;
import com.team4.backend.exception.UserNotFoundException;
import com.team4.backend.model.CustomizedUser;
import com.team4.backend.model.Movie;
import com.team4.backend.model.MovieTheater;
import com.team4.backend.model.Ticket;
import com.team4.backend.repository.MovieRepository;
import com.team4.backend.repository.MovieTheaterRepository;
import com.team4.backend.repository.TicketRepository;
import com.team4.backend.repository.UserRepository;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;

import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private MovieTheaterRepository movieTheaterRepository;

    @Autowired
    private TicketRepository ticketRepository;

    public Optional<User> findByToken(String token) {
        Optional<CustomizedUser> customer = userRepository.findByToken(token);
        if (customer.isPresent()) {
            CustomizedUser customer1 = customer.get();
            User customizedUser = new User(customer1.getUsername(), customer1.getPassword(), true, true, true, true,
                    AuthorityUtils.createAuthorityList("USER"));
            return Optional.of(customizedUser);
        }
        return Optional.empty();
    }

    public ResponseEntity<?> authenticateUser(@RequestBody Map<String, String> userInfo) throws UserNotFoundException {
        try {
            String username = userInfo.get("email");
            String password = userInfo.get("password");
            Optional<CustomizedUser> customer = userRepository.login(username, password);
            if (customer.isPresent()) {
                String token = UUID.randomUUID().toString();
                CustomizedUser customizedUser = customer.get();
                customizedUser.setToken(token);
                userRepository.save(customizedUser);
                Map<String, String> response = Map.of("status", "200", "tokenType", "Bearer", "accessToken", token, "username", customizedUser.getUsername(), "id", String.valueOf(customizedUser.getId()));
                HttpHeaders responseHeaders = new HttpHeaders();
                return ResponseEntity.ok().headers(responseHeaders).body(response);
            }
        } catch (UserNotFoundException exception) {
            throw new UserNotFoundException("User Not Found!");
        }

        return new ResponseEntity<>("User Not Found", HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<?> registerUser(@RequestBody Map<String, String> userInfo) {
        String username = userInfo.get("username");
        String email = userInfo.get("email");
        String password = userInfo.get("password");

        CustomizedUser customizedUser = new CustomizedUser();
        customizedUser.setUsername(username);
        customizedUser.setEmail(email);
        customizedUser.setPassword(password);
        String token = UUID.randomUUID().toString();
        customizedUser.setToken(token);

        userRepository.save(customizedUser);

        Map<String, String> response = Map.of("status", "200", "username", username, "id", String.valueOf(customizedUser.getId()), "accessToken", token);
        HttpHeaders responseHeaders = new HttpHeaders();
        return ResponseEntity.ok().headers(responseHeaders).body(response);

    }

    public ResponseEntity<?> getUserInfo(@RequestHeader Map<String, String> userInfo) throws UserNotFoundException {
        try {
            Optional<CustomizedUser> customizedUser = userRepository.findByToken(userInfo.get("authorization"));
            if (customizedUser.isPresent()) {
                Map<String, String> response = Map.of("status", "200", "username", customizedUser.get().getUsername(), "id", String.valueOf(customizedUser.get().getId()));
                HttpHeaders responseHeaders = new HttpHeaders();
                return ResponseEntity.ok().headers(responseHeaders).body(response);
            }
        } catch (UserNotFoundException exception) {
            throw new UserNotFoundException("User Not Found");
        }
        return new ResponseEntity<>("User Not Found", HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<?> getBookingInfo(@PathVariable("userId") String userIdStr, @RequestHeader MultiValueMap<String, String> header) throws UserNotFoundException, InvalidTicketInfoException {
        try {
            Long userId = Long.parseLong(userIdStr);
            Optional<CustomizedUser> userQuery = userRepository.findById(userId);
            JsonArray jsonArray = new JsonArray();
            if (userQuery.isPresent()) {
                CustomizedUser user = userQuery.get();
                List<Ticket> ticketList = user.getTickets();
                HashMap<HashMap<Long, String>, List<Long>> filterBookingByMovie = new HashMap<>();
                for (Ticket currTicket : ticketList) {
                    HashMap<Long, String> movieInfo = new HashMap<>();
                    movieInfo.put(currTicket.getMovieId(), currTicket.getTicketTime());
                    if (!filterBookingByMovie.containsKey(movieInfo)) {
                        List<Long> seatInfo = new ArrayList<>();
                        seatInfo.add(currTicket.getId());
                        filterBookingByMovie.put(movieInfo, seatInfo);
                    } else {
                        filterBookingByMovie.get(movieInfo).add(currTicket.getId());
                    }
                }
                for (HashMap<Long, String> movieInfo : filterBookingByMovie.keySet()) {
                    Long movieId = (Long) movieInfo.keySet().toArray()[0];
                    String movieTime = movieInfo.get(movieId);
                    Optional<Movie> movieQuery = movieRepository.findMovieById(movieId);
                    if (movieQuery.isPresent()) {
                        try {
                            Movie currMovie = movieQuery.get();
                            Optional<MovieTheater> theaterQuery = movieTheaterRepository.findById(currMovie.getMovieTheaterId());
                            if (theaterQuery.isPresent()) {
                                MovieTheater currMovieTheater = theaterQuery.get();
                                JsonObject jsonObject = new JsonObject();
                                jsonObject.addProperty("movieName", currMovie.getName());
                                jsonObject.addProperty("movieTime", movieTime);
                                String seatNum = "", seatIdStr = "";
                                for (Long id : filterBookingByMovie.get(movieInfo)) {
                                    seatIdStr += String.valueOf(id) + " ";
                                    Optional<Ticket> t = ticketRepository.findTicketById(id);
                                    if (t.isPresent()) {
                                        seatNum += t.get().getSeatInfo() + " ";
                                    }
                                }
                                jsonObject.addProperty("seatId", seatIdStr.trim());
                                jsonObject.addProperty("seat", seatNum.trim());
                                jsonObject.addProperty("imgUrl", currMovie.getImage());
                                jsonObject.addProperty("theater", currMovieTheater.getName());
                                jsonObject.addProperty("location", currMovieTheater.getLocation() + ' ' + currMovieTheater.getCity());
                                jsonArray.add(jsonObject);

                            }
                            continue;
                        } catch (InvalidTicketInfoException exception) {
                            throw new InvalidTicketInfoException("Invalid Ticket!");
                        }

                    } else {
                        return new ResponseEntity<>("User Not Found", HttpStatus.BAD_REQUEST);
                    }
                }
                HttpHeaders responseHeaders = new HttpHeaders(header);
                Gson gson = new Gson();
                String response = gson.toJson(jsonArray);
                return ResponseEntity.ok().headers(responseHeaders).body(response);

            }
        } catch (UserNotFoundException exception) {
            throw new UserNotFoundException("User Not Found!");
        }
        return new ResponseEntity<>("User Not Found", HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<?> cancelSeats(@RequestBody String reservedTickets) {
        reservedTickets = reservedTickets.substring(1, reservedTickets.length() - 1);
        List<Long> ticketId = new ArrayList<>();
        for (String s : reservedTickets.split(" ")) {
            ticketId.add(Long.parseLong(s));
        }
        for (Long id : ticketId) {
            Optional<Ticket> optionalTicket = ticketRepository.findTicketById(id);
            if (optionalTicket.isPresent()) {
                Ticket currTicket = optionalTicket.get();
                currTicket.setBookedOrNot(false);
                currTicket.setCustomizedUser(null);
                currTicket.setSeatInfo(null);
                ticketRepository.save(currTicket);
            } else {
                return new ResponseEntity<>("User Not Found", HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>("Booking Cancelled", HttpStatus.OK);
    }

}
