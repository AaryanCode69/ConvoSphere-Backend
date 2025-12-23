package com.example.convospherebackend.projections;

public interface UnreadCountProjection {
    String getConversationId();
    long getCount();
}
