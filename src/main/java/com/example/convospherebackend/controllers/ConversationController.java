package com.example.convospherebackend.controllers;

import com.example.convospherebackend.dto.request.CreateConversationDTO;
import com.example.convospherebackend.dto.request.EditMessageDTO;
import com.example.convospherebackend.dto.request.SendMessageDTO;
import com.example.convospherebackend.dto.response.*;
import com.example.convospherebackend.services.ConversationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{id}")
    public GetConversationDTO getConversationById(@PathVariable String id){
        return conversationService.getConversation(id);
    }
}
