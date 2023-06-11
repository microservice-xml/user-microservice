package com.example.usermicroservice.exception;

import com.example.usermicroservice.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class UserExistsException extends RuntimeException{

    public UserExistsException() {
        super("This user already exists");
    }
}
