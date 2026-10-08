package com.example.backend.dto;

import com.example.backend.entity.FileEntity;

public record FileResponse(
        Long id,
        String name,
        Long folderId
) {
    public static FileResponse from(FileEntity file){
        return new FileResponse(file.getId(), file.getName(), file.getFolder().getId());
    }
}
