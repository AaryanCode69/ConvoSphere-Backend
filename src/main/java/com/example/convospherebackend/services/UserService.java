package com.example.convospherebackend.services;

import com.example.convospherebackend.entities.User;
import com.example.convospherebackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getUserById(String id){
        return userRepository.getUserById(id);
    }


}
