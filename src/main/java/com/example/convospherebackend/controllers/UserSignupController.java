package com.example.convospherebackend.controllers;

import com.example.convospherebackend.dto.SignUpDTO;
import com.example.convospherebackend.services.LoginandSignUpService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class UserSignupController {

    private final LoginandSignUpService loginandSignUpService;

    @PostMapping("/signup")
    public ResponseEntity<String> registrationController(@Valid @RequestBody SignUpDTO signUpDTO){
        loginandSignUpService.userSignUp(signUpDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("User Saved Succesfully");
    }

}
