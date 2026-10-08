package com.example.backend.service;

import com.example.backend.dto.FileResponse;
import com.example.backend.dto.FolderContentsResponse;
import com.example.backend.dto.FolderResponse;
import com.example.backend.entity.Folder;
import com.example.backend.repository.FileRepository;
import com.example.backend.repository.FolderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FolderService {

    private final FolderRepository folderRepository;
    private final FileRepository fileRepository;

    public Folder createFolder(String name, Long parentId){
        Folder parent = null;

        if (parentId != null){
            parent = findFolder(parentId);
        }

        boolean alreadyExists;

        if (parentId == null){
            alreadyExists = folderRepository.existsByParentIsNullAndName(name);
        } else {
            alreadyExists = folderRepository.existsByParentIdAndName(parentId, name);
        }

        if (alreadyExists){
            throw new RuntimeException("Folder already exists.");
        }

        return folderRepository.save(new Folder(name, parent));
    }

    public List<Folder> getChildren(Long parentId){
        return folderRepository.findByParentId(parentId);
    }

    public List<Folder> getRootFolders(){
        return folderRepository.findByParentIsNull();
    }

    public void deleteFolder(Long folderId){
        Folder folder = findFolder(folderId);
        folderRepository.delete(folder);
    }

    public FolderContentsResponse getContents(Long folderId){
        Folder folder = findFolder(folderId);

        List<FolderResponse> folders = folderRepository.findByParentId(folderId)
                .stream()
                .map(FolderResponse::from)
                .toList();

        List<FileResponse> files = fileRepository.findByFolderId(folderId)
                .stream()
                .map(FileResponse::from)
                .toList();

        return new FolderContentsResponse(
                folder.getId(),
                folder.getName(),
                folder.getParent() != null ? folder.getParent().getId() : null,
                folders,
                files
        );
    }

    public FolderContentsResponse getRootContents(){
        List<FolderResponse> folders = folderRepository.findByParentIsNull()
                .stream()
                .map(FolderResponse::from)
                .toList();

        return new FolderContentsResponse(
                null,
                "/",
                null,
                folders,
                List.of()
        );
    }

    private Folder findFolder (Long folderId){
        return folderRepository.findById(folderId).orElseThrow(() -> new RuntimeException("Folder now found!"));
    }

}
