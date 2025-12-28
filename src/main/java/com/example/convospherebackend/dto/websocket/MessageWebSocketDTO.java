package com.example.convospherebackend.dto.websocket;

import lombok.Builder;

import java.time.Instant;

@Builder
public record MessageWebSocketDTO(
        String id,
        String conversationId,
        String senderId,
        String content,
        Instant createdAt
        ) {}
