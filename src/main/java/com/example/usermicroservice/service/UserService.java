package com.example.usermicroservice.service;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.example.usermicroservice.dto.messages.NewGuestUserMessage;
import com.example.usermicroservice.dto.NotificationConfigDto;
import com.example.usermicroservice.dto.messages.UserCreateFailedMessage;
import com.example.usermicroservice.event.EventType;
import com.example.usermicroservice.event.UserDeleteStarted;
import com.example.usermicroservice.exception.UserExistsException;
import com.example.usermicroservice.model.User;
import com.example.usermicroservice.model.enums.Role;
import com.example.usermicroservice.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import communication.BooleanResponse;
import communication.UserCommunicationServiceGrpc;
import communication.UserIdRequest;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    UserRepository userRepository;

    private final ObjectMapper objectMapper;

    private final RabbitTemplate rabbitTemplate;

    @Value("${accommodation-api.grpc.address}")
    private String accommodationApiGrpcAddress;

    @Value("${reservation-api.grpc.address}")
    private String reservationApiGrpcAddress;
    private Logger logger = LoggerFactory.getLogger(UserService.class);
    public List<User> findAll() {
        return userRepository.findAll();
    };
    public User registerUser(User user){
        try {
            if (checkIfUserExists(user.getUsername()) != null) {
                return null;
            }
            User newUser = userRepository.save(user);
            publishNewUser(newUser);
            createUserNotificationConfig(newUser);
            logger.info("Successfully created user. [ID: %d]",newUser.getId());
            return newUser;
        } catch (Exception ex) {
            publishRollback(user.getUsername());
            return null;
        }
    }

    private User checkIfUserExists(String username) throws UserExistsException {
        User existingUser = userRepository.findByUsername(username);
        return existingUser;
    }

    private void publishNewUser(User user) {
        if (user.getRole() != Role.GUEST) return;

        NewGuestUserMessage message = new NewGuestUserMessage(user.getId(), user.getUsername());
        try {
            String json = objectMapper.writeValueAsString(message);
            rabbitTemplate.convertAndSend("recommendationQueue", json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void publishRollback(String username) {
        UserCreateFailedMessage message = new UserCreateFailedMessage(username);
        try {
            String json = objectMapper.writeValueAsString(message);
            rabbitTemplate.convertAndSend("recommendationQueue", json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createUserNotificationConfig(User newUser) {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<NotificationConfigDto> requestBody = new HttpEntity<>(NotificationConfigDto.builder().userId(newUser.getId()).build());
        ResponseEntity<String> response;
        if(newUser.getRole().equals(Role.HOST)) {
             response = restTemplate.exchange("http://notification-api:8088/config/host", HttpMethod.POST,requestBody,String.class);
        } else{
            response = restTemplate.exchange("http://notification-api:8088/config/guest", HttpMethod.POST,requestBody,String.class);
        }
    }

    public User changeUserInfo(User newUserInfo){
        Optional<User> user = userRepository.findById(newUserInfo.getId());
        user.get().setLocation(newUserInfo.getLocation());
        user.get().setEmail(newUserInfo.getEmail());
        user.get().setUsername(newUserInfo.getUsername());
        if(!newUserInfo.getPassword().isBlank())
        {
            user.get().setPassword(newUserInfo.getPassword());
        }
        user.get().setFirstName(newUserInfo.getFirstName());
        user.get().setLastName(newUserInfo.getLastName());
        user.get().setPhoneNumber(newUserInfo.getPhoneNumber());
        user.get().setPenalties(newUserInfo.getPenalties());

        User u = userRepository.save(user.get());
        logger.info("Successfully edit user. [ID: %d]", u.getId());
        return u;
    }
    public boolean deleteUser(Long id){
        BooleanResponse response;
        User user = getById(id);
        if(user.getRole() == Role.GUEST){
            ManagedChannel channel = ManagedChannelBuilder.forAddress(reservationApiGrpcAddress, 9095)
                    .usePlaintext()
                    .build();
            UserCommunicationServiceGrpc.UserCommunicationServiceBlockingStub blockingStub = UserCommunicationServiceGrpc.newBlockingStub(channel);
            response = blockingStub.getReservation(UserIdRequest.newBuilder().setId(id).build());
            if (channel != null && !channel.isShutdown()) {
                channel.shutdown();
            }
        } else {
            ManagedChannel channel = ManagedChannelBuilder.forAddress(accommodationApiGrpcAddress, 9094)
                    .usePlaintext()
                    .build();
            communication.UserAccommodationServiceGrpc.UserAccommodationServiceBlockingStub blockingStub = communication.UserAccommodationServiceGrpc.newBlockingStub(channel);
            response = blockingStub.checkForDelete(UserIdRequest.newBuilder().setId(id).build());
            UserDeleteStarted userDeleteMessage = new UserDeleteStarted(LocalDateTime.now(), EventType.DELETE_USER_STARTED, user.getId());
            try {
                String json = objectMapper.writeValueAsString(userDeleteMessage);
                rabbitTemplate.convertAndSend("myQueue", json);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (channel != null && !channel.isShutdown()) {
                channel.shutdown();
            }
        }

        if(response.getAvailable()) {
            user.setDeleted(true);
            userRepository.save(user);
            return true;
        }

        return false;
    }

    public User loadUserByUsername(String username) {
        User user = userRepository.findByUsername(username);

        if (user == null || user.isDeleted()) {

            return null;
        }

        return user;
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
        logger.info("Successfully inc penalties of user [ID: %d]",id);
    }
}
