package com.example.convospherebackend.dto.websocket;

import com.example.convospherebackend.enums.MessageType;
import lombok.Builder;

import java.time.Instant;

@Builder
public record PresenceEventDTO(String userId,String conversationId,MessageType messageType,Instant timestamp) {}
