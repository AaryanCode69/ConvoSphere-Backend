package com.example.convospherebackend.dto;

import com.example.convospherebackend.entities.Member;
import com.example.convospherebackend.enums.ConversationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@Builder
public class CreateConversationDTO {

    @NotNull
    private ConversationType type;

    @NotBlank
    @NotNull
    private String title;

    @NotEmpty
    private List<String> membersId;
}
