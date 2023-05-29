package com.example.usermicroservice.controller;

import com.example.usermicroservice.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class ExampleController {

    private final ReservationService reservationService;

    @GetMapping
    public String ispisi() {
        return "Ok";
    }


    @GetMapping("/host/{id}")
    public ResponseEntity test(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(reservationService.findAllByHostId(id));
    }

    @GetMapping("calc/{id}")
    public ResponseEntity test1(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(reservationService.calculateHighlighted(id));
    }
}
