package com.example.convospherebackend.controllers;

import com.example.convospherebackend.dto.ConversationResponseDTO;
import com.example.convospherebackend.dto.CreateConversationDTO;
import com.example.convospherebackend.dto.GetConversationDTO;
import com.example.convospherebackend.services.ConversationService;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/conversations")
public class ConversationController {

    private final ConversationService conversationService;

    @PostMapping("/create")
    public ConversationResponseDTO createConversation(@Valid @RequestBody CreateConversationDTO createConversationDTO) {
        return conversationService.createConversation(createConversationDTO);
    }

    @GetMapping()
    public Page<GetConversationDTO> getAllConversations(@RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "10") int size){
        return conversationService.getAllUserConversations(page,size);
    }

}
