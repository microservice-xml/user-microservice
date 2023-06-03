package com.example.usermicroservice.mapper;

import com.example.usermicroservice.model.User;
import communication.RegisterUser;
import communication.RegisterUserAvgGrade;
import communication.Role;
import communication.UserDetailsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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
        return role == Role.GUEST ? com.example.usermicroservice.model.enums.Role.GUEST : com.example.usermicroservice.model.enums.Role.HOST;
    }

    private static Role convertToMessageRole(com.example.usermicroservice.model.enums.Role role) {
        return role.equals(com.example.usermicroservice.model.enums.Role.GUEST) ?  Role.GUEST: Role.HOST;
    }

    public static RegisterUser convertFromUserToRegisterUser(User user){
        return RegisterUser.newBuilder()
                .setLocation(user.getLocation())
                .setEmail(user.getEmail())
                .setUsername(user.getUsername())
                .setPassword(user.getPassword())
                .setFirstName(user.getFirstName())
                .setLastName(user.getLastName())
                .setPhoneNumber(user.getPhoneNumber())
                .setPenalties(user.getPenalties())
                .setRole(convertToMessageRole(user.getRole()))
                .build();
    }

    public static RegisterUserAvgGrade convertFromUserToRegisterUserAvgGrade(User user){
        return RegisterUserAvgGrade.newBuilder()
                .setLocation(user.getLocation())
                .setEmail(user.getEmail())
                .setUsername(user.getUsername())
                .setPassword(user.getPassword())
                .setFirstName(user.getFirstName())
                .setLastName(user.getLastName())
                .setPhoneNumber(user.getPhoneNumber())
                .setPenalties(user.getPenalties())
                .setRole(convertToMessageRole(user.getRole()))
                .setAvgGrade(user.getAvgGrade())
                .build();
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
    public static User convertUserGrpcToUser(communication.User communicationUser) {
        return User.builder()
                .id(communicationUser.getId())
                .location(communicationUser.getLocation())
                .email(communicationUser.getEmail())
                .username(communicationUser.getUsername())
                .firstName(communicationUser.getFirstName())
                .lastName(communicationUser.getLastName())
                .phoneNumber(communicationUser.getPhoneNumber())
                .penalties(communicationUser.getPenalties())
                .password(communicationUser.getPassword())
                .build();
    }

    public static communication.User convertUserToUserGrpc(User user) {
        String password = user.getPassword() != null ? user.getPassword() : ""; // Set password to an empty string if it's null

        communication.User request = communication.User.newBuilder()
                .setId(user.getId())
                .setLocation(user.getLocation())
                .setEmail(user.getEmail())
                .setUsername(user.getUsername())
                .setPassword(password)
                .setFirstName(user.getFirstName())
                .setLastName(user.getLastName())
                .setRole(convertToMessageRole(user.getRole()))
                .setPhoneNumber(user.getPhoneNumber())
                .setPenalties(user.getPenalties())
                .build();

        return request;
    }


    public static List<communication.User> convertUsersToUsersGrpc(List<User> userList) {
        List<communication.User> request = new ArrayList<>();

        for (User user : userList) {
            communication.User grpcUser = convertUserToUserGrpc(user);

            request.add(grpcUser);
        }

        return request;
    }

}
