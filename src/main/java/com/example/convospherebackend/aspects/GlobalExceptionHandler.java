package com.example.convospherebackend.aspects;

import com.example.convospherebackend.exception.EmailAlreadyExistsException;
import com.example.convospherebackend.exception.InvalidAuthenticationPrincipalException;
import com.example.convospherebackend.exception.InvalidConversationMemberException;
import com.example.convospherebackend.exception.ResourceNotFoundException;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<?>> emailAlreadyExistsExceptionHandler(EmailAlreadyExistsException exception){
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(
                        ApiResponse.error(
                                ApiError.builder()
                                        .code(HttpStatus.CONFLICT)
                                        .message("Email already exists")
                                        .build()
                        )
                );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleValidationException(MethodArgumentNotValidException exception){
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        ApiResponse.error(
                                ApiError.builder()
                                        .code(HttpStatus.BAD_REQUEST)
                                        .message(exception.getMessage())
                                        .build()
                        )

                );

    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ApiResponse<?>> handleJwtException(JwtException exception){
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(
                        ApiResponse.error(
                                ApiError.builder()
                                        .code(HttpStatus.UNAUTHORIZED)
                                        .message(exception.getMessage())
                                        .build()
                        )
                );
    }

    @ExceptionHandler({AuthenticationException.class,InvalidAuthenticationPrincipalException.class})
    public ResponseEntity<ApiResponse<?>> handleAuthException(RuntimeException exception){
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(
                        ApiResponse.error(
                                ApiError.builder()
                                        .code(HttpStatus.UNAUTHORIZED)
                                        .message(exception.getMessage())
                                        .build()
                        )
                );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleGeneralException(Exception exception){
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        ApiResponse.error(
                                ApiError.builder()
                                        .code(HttpStatus.INTERNAL_SERVER_ERROR)
                                        .message(exception.getMessage())
                                        .build()
                        )
                );
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleResourceNotFound(ResourceNotFoundException exception){
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                        ApiResponse.error(
                                ApiError.builder()
                                        .code(HttpStatus.NOT_FOUND)
                                        .message(exception.getMessage())
                                        .build()
                        )
                );
    }

    @ExceptionHandler(InvalidConversationMemberException.class)
    public ResponseEntity<ApiResponse<?>> handleInvalidConversationMember(InvalidConversationMemberException exception){
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(
                        ApiResponse.error(
                                ApiError.builder()
                                        .code(HttpStatus.FORBIDDEN)
                                        .message(exception.getMessage())
                                        .build()
                        )
                );
    }
}
