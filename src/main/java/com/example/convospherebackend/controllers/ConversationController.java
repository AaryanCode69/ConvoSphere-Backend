package com.example.convospherebackend.controllers;

import com.example.convospherebackend.dto.*;
import com.example.convospherebackend.services.ConversationService;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/{id}")
    public GetConversationDTO getConversationById(@PathVariable String id){
        return conversationService.getConversation(id);
    }

    @PostMapping("/{convId}/messages")
    public MessageResponseDTO sendMessage(@PathVariable String convId, @Valid @RequestBody SendMessageDTO sendMessageDTO){
        return conversationService.sendMessageToConv(convId,sendMessageDTO);
    }

    @GetMapping("/{convId}/messages")
    public Page<GetMessageDTO> getMessages(@PathVariable String convId,@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size){
        return conversationService.getMessageforConv(convId,page,size);
    }

    @PatchMapping("/{convId}/messages/{messageId}")
    public MessageEditResponseDTO editMessage(@PathVariable String convId,@PathVariable String messageId,@Valid @RequestBody EditMessageDTO editMessageDTO){
        return conversationService.editMessage(convId,messageId,editMessageDTO);
    }

    @DeleteMapping("/{convId}/messages/{messageId}")
    public DeleteMessageResponseDTO deleteMessage(@PathVariable String convId,@PathVariable String messageId){
        return conversationService.deleteMessage(convId,messageId);
    }

    @PostMapping("/{convId}/read")
    public ResponseEntity<?> readMessage(@PathVariable String convId){
        conversationService.readMessage(convId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
