package com.example.convospherebackend.entities;

import com.example.convospherebackend.enums.GroupRoles;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Member {
    private String userId;
    private GroupRoles role;
    private Instant joinedAt;
    private Instant leftAt;
    private boolean isMuted;
    private Instant lastReadAt;
}
