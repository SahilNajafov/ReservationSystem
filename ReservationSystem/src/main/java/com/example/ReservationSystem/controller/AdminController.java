package com.example.ReservationSystem.controller;

import com.example.ReservationSystem.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class AdminController {

    @Autowired
    UserService userService;
    @GetMapping("/admin")
    public String adminPage(Model model){
        model.addAttribute("users",userService.getAllUsers());
        return "admin";
    }
    @GetMapping("/admin/login")
    public String adminLogIn(){
        return "admin/admin-login";
    }
}
