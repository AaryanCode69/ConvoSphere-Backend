package com.example.convospherebackend.projections;

import com.example.convospherebackend.enums.MessageType;

import java.time.Instant;

public interface MessageProjection {
    String getId();
    String getSenderId();
    String getContent();
    MessageType getMessageType();
    String getMediaUrl();
    Instant getCreatedAt();
    boolean isDeleted();
    Instant getEditedAt();
    boolean isEdited();
}
