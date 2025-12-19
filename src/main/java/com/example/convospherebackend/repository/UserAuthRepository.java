package com.example.convospherebackend.repository;

import com.example.convospherebackend.entities.UserAuthProvider;
import com.example.convospherebackend.enums.LoginAuthProvider;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserAuthRepository extends MongoRepository<UserAuthProvider,String> {

    boolean existsByProviderAndProviderUserId(LoginAuthProvider loginAuthProvider, String providerUserId);
}
