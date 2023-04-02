package com.example.usermicroservice.controller;

import com.example.usermicroservice.model.User;
import com.example.usermicroservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-registration")
@RequiredArgsConstructor
public class UserController {
    UserService userService;
    @GetMapping
    public List<User> findAll() {
        return userService.findAll();
    }
    @PostMapping
    public ResponseEntity registerUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.registerUser(user));
    }
    @PostMapping
    public User changeUserInfo(@RequestBody User newUser) {
        return userService.changeUserInfo(newUser);
    }
}
