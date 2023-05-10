package com.example.usermicroservice.controller;

import com.example.usermicroservice.dto.UserDetailsResponseDto;
import com.example.usermicroservice.model.User;
import com.example.usermicroservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    @Autowired
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
    @DeleteMapping("/remove/{id}")
    public void deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
    }

    @GetMapping("/user-details/{username}")
    public ResponseEntity<UserDetailsResponseDto> getUserDetails(@PathVariable("username") String username) {
        User user = userService.loadUserByUsername(username);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        UserDetailsResponseDto dto = new UserDetailsResponseDto(user.getId(), user.getEmail(), user.getPassword(), user.getRole(), user.getPenalties());
        return ResponseEntity.ok(dto);
    }
}
