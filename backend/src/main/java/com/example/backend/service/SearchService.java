package com.example.backend.service;

import com.example.backend.entity.FileEntity;
import com.example.backend.exception.InvalidRequestException;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.repository.FileRepository;
import com.example.backend.repository.FolderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final FileRepository fileRepository;
    private final FolderRepository folderRepository;

    public List<FileEntity> search(String query, Long parentFolderId){

        if (query == null || query.isBlank()){
            throw new InvalidRequestException("Search cannot be empty query!");
        }

        query = query.trim();

        if (parentFolderId != null) {
            folderRepository.findById(parentFolderId).orElseThrow(() -> new ResourceNotFoundException("Folder was not found!"));

            return fileRepository.searchByNameInFolder(
                    query,
                    parentFolderId,
                    PageRequest.of(0, 10)
            );
        }

        return fileRepository.searchByName(
                query,
                PageRequest.of(0, 10)
        );
    }
}
