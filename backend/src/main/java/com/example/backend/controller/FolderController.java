package com.example.backend.controller;

import com.example.backend.dto.CreateFolderRequest;
import com.example.backend.dto.FolderContentsResponse;
import com.example.backend.dto.FolderResponse;
import com.example.backend.entity.Folder;
import com.example.backend.service.FolderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/folders")
@RequiredArgsConstructor
public class FolderController {

    private final FolderService folderService;

    @GetMapping("/root/contents")
    public FolderContentsResponse getRootContents(){
        return folderService.getRootContents();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FolderResponse createFolder(@Valid @RequestBody CreateFolderRequest request){
        Folder folder = folderService.createFolder(request.name(), request.parentId());
        return FolderResponse.from(folder);
    }

    @GetMapping("/root")
    public List<FolderResponse> getRootFolders(){
        return folderService.getRootFolders()
                .stream()
                .map(FolderResponse::from)
                .toList();
    }

    @GetMapping("/{parentId}/children")
    public List<FolderResponse> getChildren(@PathVariable Long parentId){
        return folderService.getChildren(parentId)
                .stream()
                .map(FolderResponse::from)
                .toList();
    }

    @DeleteMapping("/{folderId}")
    public void deleteFolder(@PathVariable Long folderId){
        folderService.deleteFolder(folderId);
    }

    @GetMapping("/{folderId}/contents")
    public FolderContentsResponse getContents(@PathVariable Long folderId){
        return folderService.getContents(folderId);
    }

}
