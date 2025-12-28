package com.example.convospherebackend.dto.websocket;

import lombok.Builder;

import java.time.Instant;

@Builder
public record ReadReceiptWebSocketDTO(String conversationId,
                                      String userId,
                                      Instant lastReadAt) {
}
