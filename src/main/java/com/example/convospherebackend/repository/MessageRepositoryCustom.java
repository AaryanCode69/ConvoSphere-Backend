package com.example.convospherebackend.repository;

import com.example.convospherebackend.projections.UnreadCountProjection;

import java.time.Instant;
import java.util.List;

public interface MessageRepositoryCustom {
    List<UnreadCountProjection> countUnreadByConversationIds(
            List<String> conversationIds,
            Instant fallbackTime
    );
}
