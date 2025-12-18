package com.example.convospherebackend.entities;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.jspecify.annotations.Nullable;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.Collection;
import java.util.List;


@Document("users")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User implements UserDetails {

    @Id
    private String id;

    @Indexed(unique = true)
    private String email;

    @Indexed(unique = true)
    private String username;

    @JsonIgnore
    private String hashedPassword;

    private String displayName;

    private String avatarUrl;

    @Builder.Default
    private boolean isActive = true;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public @Nullable String getPassword() {
        return hashedPassword;
    }
}
