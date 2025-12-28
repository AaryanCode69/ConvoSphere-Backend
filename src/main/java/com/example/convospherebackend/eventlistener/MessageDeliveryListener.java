package com.example.convospherebackend.eventlistener;

import com.example.convospherebackend.dto.websocket.MessageWebSocketDTO;
import com.example.convospherebackend.events.MessageSentEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class MessageDeliveryListener {

    private final SimpMessagingTemplate messagingTemplate;

    @Async("websocketExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onMessageSent(MessageSentEvent event) {

        log.debug("Delivering message {} to conversation {}", event.messageId(), event.conversationId());

        try {
            messagingTemplate.convertAndSend("/topic/conversation/" + event.conversationId(), MessageWebSocketDTO.
                    builder()
                    .id(event.messageId())
                    .content(event.content())
                    .conversationId(event.conversationId())
                    .createdAt(event.createdAt())
                    .senderId(event.senderId())
                    .build());
        }catch (Exception e) {
            log.error(e.getMessage());
        }

    }

}
