package com.example.convospherebackend.websocket;

import com.example.convospherebackend.dto.websocket.PresenceEventDTO;
import com.example.convospherebackend.enums.MessageType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.time.Instant;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketListener {

    private final SimpMessageSendingOperations messagingTemplate;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        log.debug("Received a new web socket connection");
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        Map<String,Object> attributes = accessor.getSessionAttributes();
        if(attributes==null) return;

        Object userIdObj = attributes.get("userId");
        Object conversationIdObj = attributes.get("conversationId");
        if(userIdObj==null || conversationIdObj==null){
            log.debug("Websocket disconnect without context");
            return;
        }

        String userId = userIdObj.toString();
        String conversationId = conversationIdObj.toString();

        PresenceEventDTO eventDTO = PresenceEventDTO.builder()
                .userId(userId)
                .conversationId(conversationId)
                .messageType(MessageType.LEAVE)
                .timestamp(Instant.now())
                .build();

        messagingTemplate.convertAndSend(
                "/topic/conversation/" + conversationId,
                eventDTO
        );

    }

}
