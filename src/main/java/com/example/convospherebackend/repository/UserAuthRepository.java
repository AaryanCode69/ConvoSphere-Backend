package com.example.convospherebackend.repository;

import com.example.convospherebackend.entities.UserAuthProvider;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserAuthRepository extends MongoRepository<UserAuthProvider,String> {
}
