package com.example.convospherebackend.events;

import lombok.Builder;

import java.time.Instant;

@Builder
public record TypingEvent(String userId,String conversationId, boolean typing, Instant timestamp) {
}
