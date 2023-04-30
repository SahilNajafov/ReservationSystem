package com.example.ReservationSystem.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private String name;
    private String surname;
    private String number;
    private String email;
    private String password;
}
