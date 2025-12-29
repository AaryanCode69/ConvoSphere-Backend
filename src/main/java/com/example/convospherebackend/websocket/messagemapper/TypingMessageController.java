package com.example.convospherebackend.websocket.messagemapper;

import com.example.convospherebackend.dto.websocket.TypingDTO;
import com.example.convospherebackend.events.TypingEvent;
import com.example.convospherebackend.exception.InvalidConversationMemberException;
import com.example.convospherebackend.repository.ConversationRepository;
import com.example.convospherebackend.services.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.Instant;

@Controller
@RequiredArgsConstructor
public class TypingMessageController {

    private final SecurityUtils securityUtils;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final ConversationRepository conversationRepository;

    @MessageMapping("/conversation/{conversationId}/typing")
    public void typingController(@PathVariable String conversationId, TypingDTO typingDTO) {
        String userId = securityUtils.getCurrentUser().getId();

        conversationRepository.findByIdAndMembersUserId(conversationId, userId)
                .orElseThrow(() -> new InvalidConversationMemberException("User not part of this conversation"));

        applicationEventPublisher.publishEvent(TypingEvent.builder()
                .userId(userId)
                .conversationId(conversationId)
                .typing(typingDTO.typing())
                .timestamp(Instant.now())
                .build()
        );

    }

}
