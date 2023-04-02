package com.example.usermicroservice.model;

import com.example.usermicroservice.model.enums.Role;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class User {
    @Id
    private Long Id;
    @Column
    private String username;
    @Column
    private String password;
    @Column
    private String name;
    @Column
    private String lastName;
    @Column
    private String email;
    @Column
    private String location;
    @Column
    private Role role;
}
