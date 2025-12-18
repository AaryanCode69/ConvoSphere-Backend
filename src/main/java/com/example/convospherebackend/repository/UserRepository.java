package com.example.convospherebackend.repository;

import com.example.convospherebackend.entities.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User,String> {
    boolean existsByEmail(String email);

    User getUserById(String id);
    User getUserByEmail(String email);
}
