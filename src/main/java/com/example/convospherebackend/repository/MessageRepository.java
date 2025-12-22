package com.example.convospherebackend.repository;

import com.example.convospherebackend.entities.Messages;
import com.example.convospherebackend.views.MessageView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MessageRepository extends MongoRepository<Messages,String> {

    Page<MessageView> findByConversationId(String conversationId, Pageable pageable);

}
