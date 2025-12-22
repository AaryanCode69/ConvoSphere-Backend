package com.example.convospherebackend.services;

import com.example.convospherebackend.dto.*;
import com.example.convospherebackend.entities.Conversations;
import com.example.convospherebackend.entities.Member;
import com.example.convospherebackend.entities.Messages;
import com.example.convospherebackend.entities.User;
import com.example.convospherebackend.enums.GroupRoles;
import com.example.convospherebackend.exception.InvalidAuthenticationPrincipalException;
import com.example.convospherebackend.exception.InvalidConversationMemberException;
import com.example.convospherebackend.exception.ResourceNotFoundException;
import com.example.convospherebackend.repository.ConversationRepository;
import com.example.convospherebackend.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;



@Service
@RequiredArgsConstructor
public class ConversationService {

    private final ConversationRepository conversationRepository;

    private final MessageRepository messageRepository;

    private final ModelMapper modelMapper;

    private final SecurityUtils securityUtils;

    @Transactional
    public ConversationResponseDTO createConversation(CreateConversationDTO createConversationDTO) {
        User creator = securityUtils.getCurrentUser();

        String creatorId = creator.getId();

        Set<String> uniqueMemberIds = new HashSet<>(createConversationDTO.getMembersId());
        List<Member> members = new ArrayList<>();

        members.add(
                Member.builder()
                        .role(GroupRoles.ADMIN)
                        .userId(creatorId)
                        .joinedAt(Instant.now())
                        .build()
        );

        for(String memberId : uniqueMemberIds) {
            if(!memberId.equals(creatorId)){
                members.add(
                        Member.builder()
                                .userId(memberId)
                                .role(GroupRoles.MEMBER)
                                .joinedAt(Instant.now())
                                .build()
                );
            }
        }

        Conversations conversations = Conversations.builder()
                .createdBy(creatorId)
                .type(createConversationDTO.getType())
                .members(members)
                .title(createConversationDTO.getTitle())
                .build();
        conversationRepository.save(conversations);

        return ConversationResponseDTO.builder()
                .id(conversations.getId())
                .title(conversations.getTitle())
                .build();

    }


    public Page<GetConversationDTO> getAllUserConversations(int pgNumber,int size) {
        User creator = securityUtils.getCurrentUser();

        String userId = creator.getId();
        Pageable pageable = PageRequest.of(pgNumber, size,Sort.by(Sort.Direction.DESC, "updatedAt"));


        Page<Conversations> conversations = conversationRepository.findByMembersUserId(userId,pageable);

        return conversations.map(
                conversation -> {
                    return modelMapper.map(conversation, GetConversationDTO.class);
                }
        );
    }

    @Transactional(readOnly = true)
    public GetConversationDTO getConversation(String id) {
        User creator = securityUtils.getCurrentUser();

        String userId = creator.getId();

        if (!conversationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Conversation not found");
        }

        Conversations conversation =
                conversationRepository.findByIdAndMembersUserId(id, userId)
                        .orElseThrow(() ->
                                new InvalidConversationMemberException("Not a member of this conversation")
                        );

        return modelMapper.map(conversation, GetConversationDTO.class);
    }

    @Transactional
    public MessageResponseDTO sendMessageToConv(String convId, SendMessageDTO sendMessageDTO) {
        User creator = securityUtils.getCurrentUser();
        String userId = creator.getId();

        Conversations conversation =
                conversationRepository.findByIdAndMembersUserId(convId, userId)
                        .orElseThrow(() ->
                                new InvalidConversationMemberException("Not a member of this conversation")
                        );
        Messages message = Messages.builder()
                .content(sendMessageDTO.getContent())
                .conversationId(conversation.getId())
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
}
