package com.example.convospherebackend.events;

import java.time.Instant;

public record MessageSentEvent(
        String messageId,
        String conversationId,
        String senderId,
        String content,
        Instant createdAt
) {}
