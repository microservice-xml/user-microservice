package com.example.usermicroservice.dto;

import com.example.usermicroservice.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@AllArgsConstructor
@Data
public class UserDetailsResponseDto {

    private Long id;

    private String email;

    private String password;

    private Role role;

    private int penalties;
}
