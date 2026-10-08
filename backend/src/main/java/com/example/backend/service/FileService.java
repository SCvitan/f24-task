package com.example.backend.service;

import com.example.backend.entity.FileEntity;
import com.example.backend.entity.Folder;
import com.example.backend.repository.FileRepository;
import com.example.backend.repository.FolderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;
    private final FolderRepository folderRepository;

    public FileEntity createFile(String name, Long folderId){
        Folder folder = folderRepository.findById(folderId).orElseThrow(() -> new RuntimeException("Folder not found!"));

        if (fileRepository.existsByFolderIdAndName(folderId, name)){
            throw new RuntimeException("File already exists!");
        }

        FileEntity file = new FileEntity(name, folder);

        return fileRepository.save(file);
    }

    public List<FileEntity> getFiles(Long folderId){
        return fileRepository.findByFolderId(folderId);
    }

    public void deleteFile(Long fileId){
        FileEntity file = fileRepository.findById(fileId).orElseThrow(() -> new RuntimeException("File was now found!"));
        fileRepository.delete(file);
    }

}
