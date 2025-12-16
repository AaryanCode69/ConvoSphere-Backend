package com.example.convospherebackend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignUpDTO {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 4,max = 50)
    private String username;

    @NotBlank
    @Size(min = 8)
    private String password;

    private String avatarUrl;

    @NotBlank
    @Size(min = 3,max = 50)
    private String displayName;

}
