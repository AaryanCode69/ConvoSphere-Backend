package com.example.convospherebackend.aspects;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Builder
@Getter
@Setter
public class ApiError {
    private HttpStatus code;
    private String message;
}
