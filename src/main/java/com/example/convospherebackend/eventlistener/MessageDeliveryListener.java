package com.example.convospherebackend.eventlistener;

import com.example.convospherebackend.dto.websocket.MessageWebSocketDTO;
import com.example.convospherebackend.dto.websocket.ReadReceiptWebSocketDTO;
import com.example.convospherebackend.dto.websocket.TypingWebSocketDTO;
import com.example.convospherebackend.events.MessageReadEvent;
import com.example.convospherebackend.events.MessageSentEvent;
import com.example.convospherebackend.events.TypingEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
@Slf4j
public class MessageDeliveryListener {

    private final SimpMessagingTemplate messagingTemplate;

    private final Map<String, Instant> typingCoolDown = new ConcurrentHashMap<>();

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

    @Async("websocketExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onMessageRead(MessageReadEvent event) {
        try{
            messagingTemplate.convertAndSend("/topic/conversation/"+event.conversationId()+"/read", ReadReceiptWebSocketDTO
            .builder()
                    .conversationId(event.conversationId())
                    .userId(event.userId())
                    .lastReadAt(event.lastReadAt())
                    .build());
        }catch (Exception e) {
            log.error("Failed to broadcast read receipt", e);
        }
    }

    @Async("websocketExecutor")
    @EventListener
    public void onUserTyping(TypingEvent event) {
        final String key = event.conversationId() + ":" + event.userId();
        if (event.typing()) {
            Instant last = typingCoolDown.get(key);
            if (last != null &&
                    Duration.between(last, Instant.now()).toMillis() < 1000) {
                return;
            }
            typingCoolDown.put(key, Instant.now());
        }
        try{
            messagingTemplate.convertAndSend("/topic/conversation/"+event.conversationId()+"/typing", TypingWebSocketDTO.builder()
                    .userId(event.userId())
                    .typing(event.typing())
                    .timestamp(event.timestamp())
                    .build());
        }catch (Exception e) {
            log.error("Failed to broadcast typing event", e);
        }
    }

}
