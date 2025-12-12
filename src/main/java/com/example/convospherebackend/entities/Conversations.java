package com.example.convospherebackend.entities;

import com.example.convospherebackend.enums.ConversationType;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Document("conversations")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Conversations {
    @Id
    private String id;

    private ConversationType type;

    private String title;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    @Builder.Default
    private boolean isArchived = false;

    private List<Member> members;
}


