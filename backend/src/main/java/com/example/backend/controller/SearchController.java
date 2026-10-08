package com.example.backend.controller;

import com.example.backend.dto.FileResponse;
import com.example.backend.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @GetMapping
    public List<FileResponse> search(@RequestParam String query, @RequestParam(required = false) Long parentFolderId){
        return searchService.search(query, parentFolderId)
                .stream()
                .map(FileResponse::from)
                .toList();
    }

}
