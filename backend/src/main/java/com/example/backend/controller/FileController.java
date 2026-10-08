package com.example.backend.controller;

import com.example.backend.dto.CreateFileRequest;
import com.example.backend.dto.FileResponse;
import com.example.backend.entity.FileEntity;
import com.example.backend.service.FileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FileResponse createFile(@Valid @RequestBody CreateFileRequest request){
        FileEntity file = fileService.createFile(
                request.name(),
                request.folderId()
        );
        return FileResponse.from(file);
    }

    @GetMapping("/folder/{folderId}")
    public List<FileResponse> getFiles(@PathVariable Long folderId){
        return fileService.getFiles(folderId)
                .stream()
                .map(FileResponse::from)
                .toList();
    }

    @DeleteMapping("/{fileId}")
    public void deleteFile(@PathVariable Long fileId){
        fileService.deleteFile(fileId);
    }

}
