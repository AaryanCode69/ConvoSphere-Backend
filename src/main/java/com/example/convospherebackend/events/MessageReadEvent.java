package com.example.convospherebackend.events;

import lombok.Builder;

import java.time.Instant;

@Builder
public record MessageReadEvent(String conversationId, String userId, Instant lastReadAt) {
}
