package com.example.convospherebackend.services;


import com.example.convospherebackend.dto.SignUpDTO;
import com.example.convospherebackend.entities.User;
import com.example.convospherebackend.exception.EmailAlreadyExistsException;
import com.example.convospherebackend.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class LoginandSignUpService {

    private final UserRepository userRepository;

    @Transactional
    public void userSignUp(SignUpDTO dto){
        String email = dto.getEmail().toLowerCase().trim();

        if(userRepository.existsByEmail(email)){
            throw new EmailAlreadyExistsException("Email Already Exists.");
        }
        User user = User.builder()
                .email(email)
                .username(dto.getUsername())
                .hashedPassword(dto.getPassword())
                .avatarUrl(dto.getAvatarUrl())
                .displayName(dto.getDisplayName())
                .build();

        try {
            userRepository.save(user);
        } catch (DuplicateKeyException ex) {
            throw new EmailAlreadyExistsException("Email already registered");
        }
        log.info("User Registered with Email: {}",email);
    }
}
