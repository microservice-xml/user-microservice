package com.example.usermicroservice.service;

import com.example.usermicroservice.model.User;
import com.example.usermicroservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    UserRepository userRepository;

    public List<User> findAll() {
        return userRepository.findAll();
    };
    public User registerUser(User user){
        return userRepository.save(user);
    }
    public User changeUserInfo(User newUserInfo){
        Optional<User> user = userRepository.findById(newUserInfo.getId());
        user.get().setLocation(newUserInfo.getLocation());
        user.get().setEmail(newUserInfo.getEmail());
        user.get().setUsername(newUserInfo.getUsername());
        if(!newUserInfo.getPassword().isEmpty())
        {
            user.get().setPassword(newUserInfo.getPassword());
        }
        user.get().setPassword(newUserInfo.getPassword());
        user.get().setFirstName(newUserInfo.getFirstName());
        user.get().setLastName(newUserInfo.getLastName());
        user.get().setPhoneNumber(newUserInfo.getPhoneNumber());
        user.get().setPenalties(newUserInfo.getPenalties());

        return userRepository.save(user.get());
    }
    public void deleteUser(Long id){
        userRepository.deleteById(id);
      /*  if(user.getRole().equals("Guest")){
            //treba dodati uslov ako Guest nema rezervacija
            userRepository.deleteById(id);
        } else
        {
            //treba dodati uslov ako Host nema zakazanih termina u svom smestaju, i brisu mu se i smestaji
            userRepository.deleteById(user.getId());
        } */
    }

    public User loadUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User getById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            return null;
        }
        return user.get();
    }

    public void incPenalties(Long id){
        User u = getById(id);
        u.setPenalties(u.getPenalties()+1);
        userRepository.save(u);
    }
}
