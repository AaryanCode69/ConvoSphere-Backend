package com.example.convospherebackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ConversationResponseDTO {

    private String id;
    private String title;
}
