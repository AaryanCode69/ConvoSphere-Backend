package com.example.convospherebackend.controllers;

import com.example.convospherebackend.dto.request.EditMessageDTO;
import com.example.convospherebackend.dto.request.SendMessageDTO;
import com.example.convospherebackend.dto.response.DeleteMessageResponseDTO;
import com.example.convospherebackend.dto.response.GetMessageDTO;
import com.example.convospherebackend.dto.response.MessageEditResponseDTO;
import com.example.convospherebackend.dto.response.MessageResponseDTO;
import com.example.convospherebackend.services.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/message")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @PostMapping("/{convId}/messages")
    public MessageResponseDTO sendMessage(@PathVariable String convId, @Valid @RequestBody SendMessageDTO sendMessageDTO){
        return messageService.sendMessageToConv(convId,sendMessageDTO);
    }

    @GetMapping("/{convId}/messages")
    public Page<GetMessageDTO> getMessages(@PathVariable String convId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size){
        return messageService.getMessageforConv(convId,page,size);
    }

    @PatchMapping("/{convId}/messages/{messageId}")
    public MessageEditResponseDTO editMessage(@PathVariable String convId, @PathVariable String messageId, @Valid @RequestBody EditMessageDTO editMessageDTO){
        return messageService.editMessage(convId,messageId,editMessageDTO);
    }

    @DeleteMapping("/{convId}/messages/{messageId}")
    public DeleteMessageResponseDTO deleteMessage(@PathVariable String convId, @PathVariable String messageId){
        return messageService.deleteMessage(convId,messageId);
    }

    @PostMapping("/{convId}/read")
    public ResponseEntity<?> readMessage(@PathVariable String convId){
        messageService.readMessage(convId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
