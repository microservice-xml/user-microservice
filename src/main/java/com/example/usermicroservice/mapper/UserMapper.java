package com.example.usermicroservice.mapper;

import com.example.usermicroservice.model.User;
import communication.RegisterUser;
import communication.Role;
import communication.UserDetailsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {

    public static UserDetailsResponse convertUserToUserDetailsResponse(final User user) {

        return UserDetailsResponse.newBuilder()
                .setId(user.getId())
                .setUsername(user.getUsername())
                .setPassword(user.getPassword())
                .setRole(convertToMessageRole(user.getRole()))
                .setPenalties(user.getPenalties()).build();
    }

    private static com.example.usermicroservice.model.enums.Role convertToEntityRole(Role role) {
        return role.equals(Role.GUEST) ? com.example.usermicroservice.model.enums.Role.GUEST : com.example.usermicroservice.model.enums.Role.HOST;
    }

    private static Role convertToMessageRole(com.example.usermicroservice.model.enums.Role role) {
        return role.equals(com.example.usermicroservice.model.enums.Role.GUEST) ?  Role.GUEST: Role.HOST;
    }

    public static User covertRegisterRequestToEntity(final RegisterUser request) {
        return User.builder()
                .location(request.getLocation())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(request.getPassword())
                .username(request.getUsername())
                .role(convertToEntityRole(request.getRole()))
                .phoneNumber(request.getPhoneNumber())
                .penalties(0)
                .build();
    }
}
