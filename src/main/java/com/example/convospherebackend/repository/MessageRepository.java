package com.example.convospherebackend.repository;

import com.example.convospherebackend.entities.Messages;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MessageRepository extends MongoRepository<Messages,String> {
}
