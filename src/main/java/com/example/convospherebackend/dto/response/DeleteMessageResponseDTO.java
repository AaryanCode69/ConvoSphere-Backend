package com.example.convospherebackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DeleteMessageResponseDTO {
    private String messageId;
    private boolean deleted;
}
