package com.example.convospherebackend.dto.request;

import com.example.convospherebackend.enums.ConversationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateConversationDTO {

    @NotNull
    private ConversationType type;

    @NotBlank
    @NotNull
    private String title;

    @NotEmpty
    private List<String> membersId;
}
