package com.example.convospherebackend.controllers;

import com.example.convospherebackend.dto.ConversationResponseDTO;
import com.example.convospherebackend.dto.CreateConversationDTO;
import com.example.convospherebackend.services.ConversationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/conversations")
public class ConversationController {

    private final ConversationService conversationService;

    @PostMapping("/create")
    public ConversationResponseDTO createConversation(@Valid @RequestBody CreateConversationDTO createConversationDTO) {

        ConversationResponseDTO conversationResponseDTO =  conversationService.createConversation(createConversationDTO);
        return  conversationResponseDTO;
    }

}
