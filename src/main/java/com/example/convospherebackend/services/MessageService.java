package com.example.convospherebackend.services;

import com.example.convospherebackend.dto.request.EditMessageDTO;
import com.example.convospherebackend.dto.request.SendMessageDTO;
import com.example.convospherebackend.dto.response.DeleteMessageResponseDTO;
import com.example.convospherebackend.dto.response.GetMessageDTO;
import com.example.convospherebackend.dto.response.MessageEditResponseDTO;
import com.example.convospherebackend.dto.response.MessageResponseDTO;
import com.example.convospherebackend.entities.Conversations;
import com.example.convospherebackend.entities.Member;
import com.example.convospherebackend.entities.Messages;
import com.example.convospherebackend.entities.User;
import com.example.convospherebackend.exception.InvalidConversationMemberException;
import com.example.convospherebackend.exception.InvalidMessageOwnerException;
import com.example.convospherebackend.exception.ResourceNotFoundException;
import com.example.convospherebackend.projections.MessageProjection;
import com.example.convospherebackend.repository.ConversationRepository;
import com.example.convospherebackend.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final SecurityUtils securityUtils;

    private final MessageRepository messageRepository;

    private final ConversationRepository conversationRepository;

    private String checkValidUser(String convId){
        User user = securityUtils.getCurrentUser();
        String userId = user.getId();

        Conversations conversation =
                conversationRepository.findByIdAndMembersUserId(convId, userId)
                        .orElseThrow(() ->
                                new InvalidConversationMemberException("Not a member of this conversation")
                        );
        return userId;
    }

    @Transactional
    public MessageResponseDTO sendMessageToConv(String convId, SendMessageDTO sendMessageDTO) {

        String userId = checkValidUser(convId);

        Messages message = Messages.builder()
                .content(sendMessageDTO.getContent())
                .conversationId(convId)
                .messageType(sendMessageDTO.getMessageType())
                .mediaUrl(sendMessageDTO.getMediaUrl())
                .senderId(userId)
                .build();

        message  = messageRepository.save(message);

        return MessageResponseDTO.builder()
                .id(message.getId())
                .conversationId(message.getConversationId())
                .createdAt(message.getCreatedAt())
                .content(message.getContent())
                .senderId(message.getSenderId())
                .build();
    }

    public Page<GetMessageDTO> getMessageforConv(String convId, int page, int size) {

        String userId = checkValidUser(convId);

        size = Math.min(size, 50);
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<MessageProjection> messages = messageRepository.findByConversationId(convId, pageable);

        return messages.map(
                message -> GetMessageDTO.builder()
                        .id(message.getId())
                        .content(message.getContent())
                        .createdAt(message.getCreatedAt())
                        .senderId(message.getSenderId())
                        .mediaUrl(message.getMediaUrl())
                        .messageType(message.getMessageType())
                        .isDeleted(message.isDeleted())
                        .editedAt(message.getEditedAt())
                        .isEdited(message.isEdited())
                        .build()
        );

    }

    @Transactional
    public MessageEditResponseDTO editMessage(String convId, String messageId, EditMessageDTO editMessageDTO) {

        String userId = checkValidUser(convId);

        Messages messages = messageRepository.findByIdAndConversationId(messageId,convId)
                .orElseThrow(()-> new ResourceNotFoundException("Message not found"));
        if(!messages.getSenderId().equals(userId)){
            throw new InvalidMessageOwnerException("User is not an Owner of this Message");
        }
        if(messages.isDeleted()){
            throw new IllegalStateException("Cannot edit a deleted message");
        }
        messages.setContent(editMessageDTO.getContent());
        messages.setEditedAt(Instant.now());
        messages.setEdited(true);
        messageRepository.save(messages);

        return MessageEditResponseDTO.builder()
                .id(messages.getId())
                .editedAt(messages.getEditedAt())
                .content(messages.getContent())
                .build();

    }

    @Transactional
    public DeleteMessageResponseDTO deleteMessage(String convId, String messageId) {

        String userId = checkValidUser(convId);

        Messages messages = messageRepository.findByIdAndConversationId(messageId,convId)
                .orElseThrow(()-> new ResourceNotFoundException("Message not found"));
        if(!messages.getSenderId().equals(userId)){
            throw new InvalidMessageOwnerException("User is not an Owner of this Message");
        }
        if(messages.isDeleted()){
            throw new IllegalStateException("Cannot delete a deleted message");
        }

        messages.setDeleted(true);
        messageRepository.save(messages);
        return DeleteMessageResponseDTO.builder()
                .deleted(true)
                .messageId(messageId)
                .build();
    }

    @Transactional
    public void readMessage(String convId) {
        User user = securityUtils.getCurrentUser();
        String userId = user.getId();

        Conversations conversation =
                conversationRepository.findByIdAndMembersUserId(convId, userId)
                        .orElseThrow(() ->
                                new InvalidConversationMemberException("Not a member of this conversation")
                        );
        for(Member member : conversation.getMembers()){
            if(member.getUserId().equals(userId) ){
                if(member.getLastReadAt()==null || member.getLastReadAt().isBefore(Instant.now())) {
                    member.setLastReadAt(Instant.now());
                    break;
                }
            }
        }
        conversationRepository.save(conversation);
    }

}
