package com.example.convospherebackend.dto.request;


import com.example.convospherebackend.enums.MessageType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SendMessageDTO {

    @NotBlank(message = "Message can be empty")
    private String content;

    @NotNull
    private MessageType messageType;

    @Builder.Default
    private String mediaUrl = null;

}
