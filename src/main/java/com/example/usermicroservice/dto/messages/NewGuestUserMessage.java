package com.example.usermicroservice.dto.messages;

import com.example.usermicroservice.event.EventType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class NewGuestUserMessage {

    private long userId;

    private String name;

    private EventType type;

    public NewGuestUserMessage() {
        type = EventType.USER_CREATED;
    }

    public NewGuestUserMessage(long userId, String name) {
        this.userId = userId;
        this.name = name;
        type = EventType.USER_CREATED;
    }
}
