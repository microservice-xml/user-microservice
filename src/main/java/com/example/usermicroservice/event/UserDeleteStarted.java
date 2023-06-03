package com.example.usermicroservice.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
public class UserDeleteStarted extends BaseEvent {

    private Long userId;

    public UserDeleteStarted(LocalDateTime timestamp, EventType type, Long userId) {
        super(timestamp, type);
        this.userId = userId;
    }
}
