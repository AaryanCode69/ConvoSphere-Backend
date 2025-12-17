package com.example.convospherebackend.aspects;

import com.example.convospherebackend.exception.EmailAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<?>> emailAlreadyExistsExceptionHandler(EmailAlreadyExistsException exception){
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(
                        new ApiResponse<>(
                                ApiError.builder()
                                        .code(HttpStatus.CONFLICT)
                                        .message("Email already exists")
                                        .build()
                        )
                );
    }

}
