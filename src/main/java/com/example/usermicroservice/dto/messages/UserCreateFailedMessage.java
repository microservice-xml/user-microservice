package com.example.usermicroservice.dto.messages;

import com.example.usermicroservice.event.EventType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class UserCreateFailedMessage {

    private String name;

    private EventType type;

    public UserCreateFailedMessage() {
        type = EventType.USER_CREATE_FAILED;
    }

    public UserCreateFailedMessage(String userId) {
        this.name = userId;
        type = EventType.USER_CREATE_FAILED;
    }
}
