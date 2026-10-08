package com.example.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateFileRequest(
        @NotBlank @Size(max = 255)
        String name,

        @NotNull
        Long folderId
) {
}
