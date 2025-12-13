package com.example.convospherebackend.entities;

import com.example.convospherebackend.enums.MessageType;
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


    private String conversationId;

    private String senderId;

    private String content;

    private MessageType messageType;

    @Builder.Default
    private String mediaUrl = null;

    @CreatedDate
    private Instant createdAt;

    private Instant editedAt;

    @Builder.Default
    private boolean isDeleted = false;
}
