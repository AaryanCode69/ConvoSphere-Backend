package com.example.convospherebackend.views;

import com.example.convospherebackend.enums.MessageType;

import java.time.Instant;

public interface MessageView {
    String getId();
    String getSenderId();
    String getContent();
    MessageType getMessageType();
    String getMediaUrl();
    Instant getCreatedAt();
    boolean isDeleted();
    Instant getEditedAt();
}
