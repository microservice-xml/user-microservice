package com.example.usermicroservice.controller;

import com.example.usermicroservice.model.User;
import com.example.usermicroservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    UserService userService;
    @GetMapping("/all")
    public ResponseEntity<List<User>> findAll() {
        return new ResponseEntity<>(userService.findAll(), OK);
    }
    @PostMapping("/registration")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        return new ResponseEntity<>(userService.registerUser(user), CREATED);
    }
    @PutMapping("/change-personal-info")
    public ResponseEntity<User> changeUserInfo(@RequestBody User newUserInfo) {
        return new ResponseEntity<>(userService.changeUserInfo(newUserInfo), CREATED);
    }
    @DeleteMapping("/remove/{user}")
    public void deleteUser(@PathVariable("user") User user){
        userService.deleteUser(user);
    }
}
