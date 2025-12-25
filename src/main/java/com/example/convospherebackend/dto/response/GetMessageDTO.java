package com.example.convospherebackend.dto.response;

import com.example.convospherebackend.enums.MessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetMessageDTO {

    private String id;
    private String senderId;
    private String content;
    private MessageType messageType;
    private String mediaUrl;
    private Instant createdAt;
    private boolean isDeleted;
    private Instant editedAt;
    private boolean isEdited;

}
