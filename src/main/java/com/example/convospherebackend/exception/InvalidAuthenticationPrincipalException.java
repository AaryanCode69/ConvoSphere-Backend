package com.example.convospherebackend.exception;

public class InvalidAuthenticationPrincipalException extends RuntimeException {
    public InvalidAuthenticationPrincipalException(String message) {
        super(message);
    }
}
