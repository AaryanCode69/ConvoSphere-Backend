package com.example.convospherebackend.dto.response;


import com.example.convospherebackend.enums.ConversationType;
import lombok.*;

import java.time.Instant;


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

    private long unreadCount;

    private boolean isArchived;

}
