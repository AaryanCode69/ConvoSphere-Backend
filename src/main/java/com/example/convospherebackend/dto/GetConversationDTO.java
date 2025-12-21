package com.example.convospherebackend.dto;

import com.example.convospherebackend.entities.Member;
import com.example.convospherebackend.enums.ConversationType;
import lombok.*;

import java.time.Instant;
import java.util.List;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetConversationDTO {

    private String id;

    private ConversationType type;

    private String title;

    private Instant createdAt;

    private boolean isArchived;

}
