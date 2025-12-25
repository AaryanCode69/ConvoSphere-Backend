package com.example.convospherebackend.services;

import com.example.convospherebackend.dto.request.CreateConversationDTO;
import com.example.convospherebackend.dto.request.EditMessageDTO;
import com.example.convospherebackend.dto.request.SendMessageDTO;
import com.example.convospherebackend.dto.response.*;
import com.example.convospherebackend.entities.Conversations;
import com.example.convospherebackend.entities.Member;
import com.example.convospherebackend.entities.Messages;
import com.example.convospherebackend.entities.User;
import com.example.convospherebackend.enums.GroupRoles;
import com.example.convospherebackend.exception.InvalidConversationMemberException;
import com.example.convospherebackend.exception.InvalidMessageOwnerException;
import com.example.convospherebackend.exception.ResourceNotFoundException;
import com.example.convospherebackend.projections.UnreadCountProjection;
import com.example.convospherebackend.repository.ConversationRepository;
import com.example.convospherebackend.repository.MessageRepository;
import com.example.convospherebackend.projections.MessageProjection;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;


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

    @Transactional(readOnly = true)
    public Page<GetConversationDTO> getAllUserConversations(int pgNumber,int size) {
        User creator = securityUtils.getCurrentUser();

        String userId = creator.getId();
        Pageable pageable = PageRequest.of(pgNumber, size,Sort.by(Sort.Direction.DESC, "updatedAt"));


        Page<Conversations> conversations = conversationRepository.findByMembersUserId(userId,pageable);

        List<String> conversationIds = conversations.stream()
                .map(Conversations::getId)
                .toList();
        Map<String, Instant> lastReadMap = new HashMap<>();

        for (Conversations c : conversations) {
            for (Member m : c.getMembers()) {
                if (m.getUserId().equals(userId)) {
                    lastReadMap.put(c.getId(), m.getLastReadAt());
                    break;
                }
            }
        }

        List<UnreadCountProjection> unreadCounts =
                messageRepository.countUnreadByConversationIds(
                        conversationIds,
                        Instant.EPOCH
                );

        Map<String, Long> unreadCountMap = unreadCounts.stream()
                .collect(Collectors.toMap(
                        UnreadCountProjection::getConversationId,
                        UnreadCountProjection::getCount
                ));

        return conversations.map(conversation -> {

            Instant lastReadAt = lastReadMap.get(conversation.getId());

            long totalUnread = unreadCountMap.getOrDefault(
                    conversation.getId(),
                    0L
            );


            if (lastReadAt != null) {
                long readCount = messageRepository.countByConversationIdAndCreatedAtBeforeAndIsDeletedFalse(
                        conversation.getId(),
                        lastReadAt
                );
                totalUnread = Math.max(totalUnread - readCount, 0);
            }

            return GetConversationDTO.builder()
                    .id(conversation.getId())
                    .title(conversation.getTitle())
                    .type(conversation.getType())
                    .createdAt(conversation.getCreatedAt())
                    .isArchived(conversation.isArchived())
                    .unreadCount(totalUnread)
                    .build();
        });
    }

    @Transactional(readOnly = true)
    public GetConversationDTO getConversation(String id) {
        User creator = securityUtils.getCurrentUser();

        String userId = creator.getId();

        Conversations conversation =
                conversationRepository.findByIdAndMembersUserId(id, userId)
                        .orElseThrow(() ->
                                new InvalidConversationMemberException("Not a member of this conversation")
                        );
        Member currMember = null;
        for(Member member : conversation.getMembers()){
            if(member.getUserId().equals(userId)){
                currMember = member;
                break;
            }
        }
        if (currMember == null) {
            throw new InvalidConversationMemberException("Member state not found");
        }

        Instant lastReadAt = currMember.getLastReadAt();

        long unreadCount = (lastReadAt == null)
                ? messageRepository.countByConversationIdAndIsDeletedFalse(conversation.getId())
                : messageRepository.countByConversationIdAndCreatedAtAfterAndIsDeletedFalse(
                conversation.getId(), lastReadAt
        );

        return GetConversationDTO.builder()
                .id(conversation.getId())
                .title(conversation.getTitle())
                .type(conversation.getType())
                .createdAt(conversation.getCreatedAt())
                .isArchived(conversation.isArchived())
                .unreadCount(unreadCount)
                .build();
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

    public Page<GetMessageDTO> getMessageforConv(String convId, int page, int size) {
        User creator = securityUtils.getCurrentUser();
        String userId = creator.getId();
        Conversations conversation =
                conversationRepository.findByIdAndMembersUserId(convId, userId)
                        .orElseThrow(() ->
                                new InvalidConversationMemberException("Not a member of this conversation")
                        );
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
        User user = securityUtils.getCurrentUser();
        String userId = user.getId();

        Conversations conversation =
                conversationRepository.findByIdAndMembersUserId(convId, userId)
                        .orElseThrow(() ->
                                new InvalidConversationMemberException("Not a member of this conversation")
                        );
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
        User user = securityUtils.getCurrentUser();
        String userId = user.getId();

        Conversations conversation =
                conversationRepository.findByIdAndMembersUserId(convId, userId)
                        .orElseThrow(() ->
                                new InvalidConversationMemberException("Not a member of this conversation")
                        );
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
