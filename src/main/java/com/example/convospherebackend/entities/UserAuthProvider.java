package com.example.convospherebackend.entities;

import com.example.convospherebackend.enums.LoginAuthProvider;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document("userauthprovider")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserAuthProvider {

    @Id
    private String id;

    @Indexed
    private String userId;

    @Indexed
    private LoginAuthProvider provider;

    @Indexed(unique = true)
    private String providerUserId;

    private String providerEmail;

    @CreatedDate
    private Instant createdAt;

}
