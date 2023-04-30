package com.example.ReservationSystem.entity;

import jakarta.persistence.*;
import lombok.Data;
@Entity
@Table(name = "clients",schema = "user_schema")
@Data
public class Client extends AbstractEntity {

    // TODO : email or number part
    // TODO: client bir muddetden sonra db den silinsin
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String surname;
    @Column(nullable = false, unique = true)
    private String number;

    @Column(nullable = false,unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    public Client() {
    }
}