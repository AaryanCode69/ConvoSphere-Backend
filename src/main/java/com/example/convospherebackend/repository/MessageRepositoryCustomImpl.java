package com.example.convospherebackend.repository;

import com.example.convospherebackend.projections.UnreadCountProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MessageRepositoryCustomImpl implements MessageRepositoryCustom{

    private final MongoTemplate mongoTemplate;

    @Override
    public List<UnreadCountProjection> countUnreadByConversationIds(List<String> conversationIds, Instant fallbackTime) {
        MatchOperation match = Aggregation.match(
                Criteria.where("conversationId").in(conversationIds)
                        .and("isDeleted").is(false)
        );

        GroupOperation group = Aggregation.group("conversationId")
                .count().as("count");

        Aggregation aggregation = Aggregation.newAggregation(match, group,Aggregation.project("count")
                .and("_id").as("conversationId"));

        return mongoTemplate
                .aggregate(aggregation, "messages", UnreadCountProjection.class)
                .getMappedResults();
    }
}
