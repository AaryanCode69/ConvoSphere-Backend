package com.example.convospherebackend.repository;

import com.example.convospherebackend.entities.Messages;
import com.example.convospherebackend.projections.MessageProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.Instant;
import java.util.Optional;

public interface MessageRepository extends MongoRepository<Messages,String>, MessageRepositoryCustom {

    Page<MessageProjection> findByConversationId(String conversationId, Pageable pageable);

    Optional<Messages> findByIdAndConversationId(String messageId, String convId);

    long countByConversationIdAndCreatedAtAfterAndIsDeletedFalse(String conversationId, Instant createdAt);

    long countByConversationIdAndIsDeletedFalse(String id);

    long countByConversationIdAndCreatedAtBeforeAndIsDeletedFalse(String id, Instant lastReadAt);
}
