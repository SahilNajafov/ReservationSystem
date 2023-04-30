package com.example.ReservationSystem.controller;

import com.example.ReservationSystem.dto.UserDTO;
import com.example.ReservationSystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/changes")
public class UserController {

    @Autowired
    UserService userService;

//    @PostMapping("/login")
//    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request){
//        return ResponseEntity.ok(userService.authenticate(request));
//    }
//    @PostMapping("/signup")
//    public ResponseEntity<String> registerNewUser(@RequestBody UserDTO request){
//        return ResponseEntity.ok(userService.register(request));
//    }
    @DeleteMapping("/edit")
    public String deletingUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userService.deleteUser(authentication.getName());
    }

    @PutMapping("/edit")
    public ResponseEntity<String> changingUserDetails(@RequestBody(required = false) UserDTO request){
        return userService.changeUserDetails(request);
    }
}
