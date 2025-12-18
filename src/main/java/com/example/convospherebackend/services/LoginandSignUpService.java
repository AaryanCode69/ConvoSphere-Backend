package com.example.convospherebackend.services;


import com.example.convospherebackend.dto.LoginDTO;
import com.example.convospherebackend.dto.LoginResponseDTO;
import com.example.convospherebackend.dto.SignUpDTO;
import com.example.convospherebackend.entities.User;
import com.example.convospherebackend.exception.EmailAlreadyExistsException;
import com.example.convospherebackend.exception.EmailNotFoundException;
import com.example.convospherebackend.exception.InvalidCredentialException;
import com.example.convospherebackend.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class LoginandSignUpService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;
    
    @Transactional
    public void userSignUp(SignUpDTO dto){
        String email = dto.getEmail().toLowerCase().trim();

        if(userRepository.existsByEmail(email)){
            throw new EmailAlreadyExistsException("Email Already Exists.");
        }
        User user = User.builder()
                .email(email)
                .username(dto.getUsername())
                .hashedPassword(passwordEncoder.encode(dto.getPassword()))
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

    @Transactional
    public LoginResponseDTO userLogin(LoginDTO loginDTO){
        String email = loginDTO.getEmail().toLowerCase().trim();
        User user = userRepository.getUserByEmail(email);
        if(user==null){
            throw new EmailNotFoundException("Email Not Found, Please Login Using a Valid Email Address");
        }
        if(!passwordEncoder.matches(loginDTO.getPassword(), user.getHashedPassword())){
            throw new InvalidCredentialException("Invalid Credentails, Please Try Again Using Valid Credentials");
        }
        return  LoginResponseDTO.builder()
                .accessToken(jwtService.generateAccessToken(user))
                .refreshToken(jwtService.generateRefreshToken(user))
                .build();
    }

}
