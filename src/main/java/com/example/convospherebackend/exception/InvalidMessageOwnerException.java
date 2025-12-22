package com.example.convospherebackend.exception;

public class InvalidMessageOwnerException extends RuntimeException {
    public InvalidMessageOwnerException(String message) {
        super(message);
    }
}
