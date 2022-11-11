package com.team4.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import com.team4.backend.services.UserService;

import java.util.Map;

@RestController
@RequestMapping("/app/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody Map<String, String> userInfo) {
        return userService.authenticateUser(userInfo);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> userInfo) {
        return userService.registerUser(userInfo);
    }

    @GetMapping("/info")
    public ResponseEntity<?> userInfo(@RequestHeader Map<String, String> userInfo) {
        return userService.getUserInfo(userInfo);
    }


    @GetMapping("/management/{userId}")
    public ResponseEntity<?> bookingInfo(@PathVariable("userId") String userIdStr, @RequestHeader MultiValueMap<String, String> header) {
        return userService.getBookingInfo(userIdStr, header);
    }

    @PostMapping("/cancel")
    public ResponseEntity<?> cancelSeats(@RequestBody String reservedTickets) {
        return userService.cancelSeats(reservedTickets);
    }
}
