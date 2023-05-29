package com.example.usermicroservice.controller;


import com.example.usermicroservice.model.Rate;
import com.example.usermicroservice.service.RateService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rate")
@RequiredArgsConstructor
public class RateController {
    @Autowired
    RateService rateService;

    @GetMapping("")
    public ResponseEntity findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(rateService.findAll());
    }

    @PostMapping("")
    public ResponseEntity rateHost(@RequestBody Rate rate){
        return ResponseEntity.status(HttpStatus.CREATED).body(rateService.rateHost(rate));
    }

    @PutMapping("/{id}")
    public ResponseEntity changeRate(@RequestBody Rate rate){
        return ResponseEntity.status(HttpStatus.CREATED).body(rateService.changeRate(rate));
    }

    @DeleteMapping("/{id}")
    public void deleteRate(@RequestBody Rate rate){
        rateService.deleteRate(rate);
    }
}
