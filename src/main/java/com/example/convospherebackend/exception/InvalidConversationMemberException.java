package com.example.convospherebackend.exception;

public class InvalidConversationMemberException extends RuntimeException {
    public InvalidConversationMemberException(String message) {
        super(message);
    }
}
