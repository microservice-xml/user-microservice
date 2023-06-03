package com.example.usermicroservice.event;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccommodationDeleteFailed {

    private Long userId;
}
