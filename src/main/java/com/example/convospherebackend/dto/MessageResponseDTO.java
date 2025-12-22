package com.example.convospherebackend.dto;

import lombok.*;

import java.time.Instant;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponseDTO {
    private String id;
    private String conversationId;
    private String senderId;
    private String content;
    private Instant createdAt;
}
