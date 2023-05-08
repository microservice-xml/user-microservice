package com.example.usermicroservice.model;

import com.example.usermicroservice.model.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users" , schema = "public")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;
    @Column(name = "location", nullable = false)
    private String location;
    @Column(name = "email", nullable = false)
    private String email;
    @Column(name = "username" , unique = true, nullable = false)
    private String username;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "first_name", nullable = false)
    private String firstName;
    @Column(name = "last_name", nullable = false)
    private String lastName;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "penalties")
    private int penalties;
    @Column(name = "role", nullable = false)
    private String role;
    //Ovde treba promeniti da je role tipa Role ali me zeza MySql vec satima i ne radi ne znam zasto
    @Column(name = "numberOfCancel")
    private int numberOfCancel;

}
