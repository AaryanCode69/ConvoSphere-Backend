package com.example.convospherebackend.dto.websocket;

import com.example.convospherebackend.enums.MessageType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Builder
public class PresenceEventDTO {
    private String userId;
    private String conversationId;
    private MessageType messageType;
    private Instant timestamp;
}
