package com.example.convospherebackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MessageEditResponseDTO {
    private String id;
    private String content;
    private Instant editedAt;
}

