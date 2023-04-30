package com.example.ReservationSystem.service;

import com.example.ReservationSystem.Enum.Role;
import com.example.ReservationSystem.config.ApplicationConfig;
import com.example.ReservationSystem.config.JwtService;
import com.example.ReservationSystem.controller.AuthenticationRequest;
import com.example.ReservationSystem.controller.AuthenticationResponse;
import com.example.ReservationSystem.dto.UserDTO;
import com.example.ReservationSystem.entity.User;
import com.example.ReservationSystem.exceptions.TakenEmailException;
import com.example.ReservationSystem.exceptions.TakenNumberException;
import com.example.ReservationSystem.exceptions.UserNotExistsException;
import com.example.ReservationSystem.exceptions.WrongEmailPasswordStructureException;
import com.example.ReservationSystem.repository.UserRepository;
import com.example.ReservationSystem.util.Regexs;
import lombok.AllArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ApplicationConfig passwordEncoder;

    @Autowired
    private final JwtService jwtService;

    @Autowired
    private final AuthenticationManager authenticationManager;


    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(UserService.class);


    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public String deleteUser(String email) {
        Optional<User> userOptional = userRepository.findUserByEmail(email);
        if (userOptional.isPresent()) {
            Long idByEmail = userRepository.findIdByEmail(email);
            userRepository.deleteById(idByEmail);
            return "user deleted!";
        } else {
            throw new UserNotExistsException();
        }

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findUserByEmail(username).orElseThrow(UserNotExistsException::new);
    }

    public String register(UserDTO request) {
        if (userRepository.findUserByEmail(request.getEmail()).isPresent()) {
            logger.error(String.valueOf(new TakenEmailException()));
            throw new TakenEmailException();
        } else {
            if (Regexs.checkMail(request.getEmail()) && Regexs.checkPassword(request.getPassword())) {
                var user = User
                        .builder()
                        .name(request.getName())
                        .surname(request.getSurname())
                        .number(request.getNumber())
                        .email(request.getEmail())
                        .password(passwordEncoder.encoder().encode(request.getPassword()))
                        .role(Role.USER)
                        .build();
                userRepository.save(user);
                return "user saved!";
            } else {
                logger.error(String.valueOf(new WrongEmailPasswordStructureException()));
                throw new WrongEmailPasswordStructureException();
            }

        }
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = userRepository.findUserByEmail(request.getEmail())
                .orElseThrow(UserNotExistsException::new);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public ResponseEntity<String> changeUserDetails(UserDTO request) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Long idByEmail = userRepository.findIdByEmail(email);
        User userById = userRepository.findUserById(idByEmail);


        if (request.getNumber() != null && !request.getNumber().equals(userById.getNumber())) {
            if (userRepository.findUserByNumber(request.getNumber()).isPresent()) {
                logger.error(String.valueOf(new TakenNumberException()));
                System.out.println(ResponseEntity.badRequest().body("Number is taken!"));
            } else {
                userById.setNumber(request.getNumber());
            }
        }
        if (request.getEmail() != null && !request.getEmail().equals(userById.getEmail())) {
            if (userRepository.findUserByEmail(request.getEmail()).isPresent()) {
                logger.error(String.valueOf(new TakenEmailException()));
                System.out.println(ResponseEntity.badRequest().body("Email is taken!"));
                throw new TakenEmailException();
            } else {
                userById.setEmail(request.getEmail());
            }
        }
        if (request.getName() != null && !request.getName().equals(userById.getName())) {
            userById.setName(request.getName());
        }
        if (request.getSurname() != null && !request.getSurname().equals(userById.getSurname())) {
            userById.setSurname(request.getSurname());
        }
        if (request.getPassword() != null && !request.getPassword().equals(userById.getPassword())) {
            userById.setPassword(passwordEncoder.encoder().encode(request.getPassword()));
        }

        userRepository.save(userById);

        return ResponseEntity.ok("User details changed succesfully!");
    }
}
