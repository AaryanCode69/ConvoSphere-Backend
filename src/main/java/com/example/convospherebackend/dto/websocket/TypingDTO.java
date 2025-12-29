package com.example.convospherebackend.dto.websocket;

import lombok.Builder;

@Builder
public record TypingDTO(boolean typing) {
}
