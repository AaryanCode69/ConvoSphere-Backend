package com.example.convospherebackend.dto.websocket;

import lombok.Builder;

import java.time.Instant;

@Builder
public record TypingWebSocketDTO(String userId, boolean typing, Instant timestamp) {
}
