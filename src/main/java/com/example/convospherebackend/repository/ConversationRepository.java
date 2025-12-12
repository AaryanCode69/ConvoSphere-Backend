package com.example.convospherebackend.repository;

import com.example.convospherebackend.entities.Conversations;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ConversationRepository extends MongoRepository<Conversations,String> {
}
