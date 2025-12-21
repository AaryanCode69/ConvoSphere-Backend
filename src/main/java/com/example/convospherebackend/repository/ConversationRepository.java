package com.example.convospherebackend.repository;

import com.example.convospherebackend.entities.Conversations;
import com.example.convospherebackend.entities.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ConversationRepository extends MongoRepository<Conversations,String> {

    Page<Conversations> findByMembersUserId(String memberId,Pageable pageable);

    Optional<Conversations> findByIdAndMembersUserId(String id, String memberId);
}
