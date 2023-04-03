package com.example.usermicroservice.service;

import com.example.usermicroservice.model.User;
import com.example.usermicroservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    UserRepository userRepository;
    public List<User> findAll() {
        return userRepository.findAll();
    };
    public User registerUser(User user){
        return userRepository.save(user);
    }
    public User changeUserInfo(User newUserInfo){
        return userRepository.save(newUserInfo);
    }
    public void deleteUser(User user){
        if(user.getRole().equals("Guest")){
            //treba dodati uslov ako Guest nema rezervacija
            userRepository.deleteById(user.getId());
        } else
        {
            //treba dodati uslov ako Host nema zakazanih termina u svom smestaju, i brisu mu se i smestaji
            userRepository.deleteById(user.getId());
        }
    }
}
