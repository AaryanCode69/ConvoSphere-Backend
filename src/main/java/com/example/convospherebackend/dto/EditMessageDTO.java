package com.example.convospherebackend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditMessageDTO {

    @NotBlank
    private String content;

}

