package com.example.convospherebackend.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
public class ConversationResponseDTO {

    private String id;
    private String title;
}
