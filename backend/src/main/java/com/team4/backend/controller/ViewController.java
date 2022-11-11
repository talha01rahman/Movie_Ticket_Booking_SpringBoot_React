package com.team4.backend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

// redirect mapping to frontend
@Controller
public class ViewController {
    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping("/{id}")
    public String index_id() {
        return "index";
    }

    @RequestMapping("/login**")
    public String index_login() {
        return "index";
    }

    @RequestMapping("/register**")
    public String index_register() {
        return "index";
    }

    @RequestMapping("/{movieid}/reserve/{ticketid}")
    public String index_booking() {
        return "index";
    }

    @RequestMapping("/{userid}/reserve_manage")
    public String index_booking_management() {
        return "index";
    }
}
