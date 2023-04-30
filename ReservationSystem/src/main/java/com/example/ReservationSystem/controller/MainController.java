package com.example.ReservationSystem.controller;

import com.example.ReservationSystem.dto.UserDTO;
import com.example.ReservationSystem.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequestMapping("/")
public class MainController {

    @Autowired
    UserService userService;

    @GetMapping(value = "main")
    public String mainPage(){
        return "main";
    }
    @GetMapping(value = "login")
    public String loginPage(Model model){
        model.addAttribute("authreq",AuthenticationRequest.builder().build());
        return "login";
    }

    @PostMapping("login")
    public String loginAuth(HttpServletRequest request,@ModelAttribute("authreq")AuthenticationRequest build){
        // TODO: bunun icine girmir
//        if(userService.loadUserByUsername(build.getEmail()).getUsername()!=null){
//            return "redirect:/login?error";
//        }
//        else if (result.hasErrors()) {
//            model.addAttribute("authreq", build);
//            return "redirect:/login?error";
//        }
//        else{
            AuthenticationResponse s = userService.authenticate(build);
            HttpSession session = request.getSession();
            session.setAttribute("Authorization", s.getToken());
            System.out.println("****** loginde qaytardigi token"+s.getToken());
            return "redirect:/main";
//        }



    }
    @GetMapping("signup")
    public String signUp(Model model){
        UserDTO userDTO = new UserDTO();
        model.addAttribute("user",userDTO);
        return "signup";
    }
    @PostMapping("signup")
    public String registerNewUser(@ModelAttribute("user") UserDTO user){
        userService.register(user);
        return "redirect:/login";
    }



}
