package com.example.convospherebackend.entities;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.Instant;

@Document("messages")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Messages {
    @Id
    private String id;

    @DocumentReference(lazy = true)
    private Conversations conversationId;

    @DocumentReference(lazy = true)
    private User senderId;

    private String content;

    @Builder.Default
    private String mediaUrl = null;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant editedAt;

    @Builder.Default
    private boolean isDeleted = false;
}
