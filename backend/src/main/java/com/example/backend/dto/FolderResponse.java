package com.example.backend.dto;

import com.example.backend.entity.Folder;

public record FolderResponse(
        Long id,
        String name,
        Long parentId
) {
    public static FolderResponse from(Folder folder){
        return new FolderResponse(
                folder.getId(),
                folder.getName(),
                folder.getParent() != null ? folder.getParent().getId() : null
        );
    }
}
