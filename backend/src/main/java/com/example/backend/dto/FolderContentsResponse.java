package com.example.backend.dto;

import java.util.List;

public record FolderContentsResponse(
        Long id,
        String name,
        Long parentId,
        List<FolderResponse> folders,
        List<FileResponse> files
) {
}
