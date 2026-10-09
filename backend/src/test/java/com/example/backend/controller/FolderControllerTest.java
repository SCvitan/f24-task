package com.example.backend.controller;

import com.example.backend.entity.Folder;
import com.example.backend.repository.FolderRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class FolderControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private FolderRepository folderRepository;

    @Test
    void testShouldCreateRootFolder() throws Exception {
        String request = """
                {
                    "name": "TestDocuments2026",
                    "parentId": null
                }
                """;

        mockMvc.perform(post("/api/folders")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(request))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("TestDocuments2026"))
                .andExpect(jsonPath("$.parentId").doesNotExist());


    }

    @Test
    void testShouldCreateSubfolder() throws Exception {
        Folder parent = folderRepository.save(
                new Folder("TestParent", null)
        );

        String request = """
                {
                    "name": "TestChild",
                    "parentId": %d
                }
                """.formatted(parent.getId());

        mockMvc.perform(post("/api/folders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("TestChild"))
                .andExpect(jsonPath("$.parentId").value(parent.getId()));
    }

    @Test
    void testShouldReturnConflictWhenCreatingDuplicateRootFolder() throws Exception {
        String request = """
            {
                "name": "DuplicateTestFolder",
                "parentId": null
            }
            """;

        mockMvc.perform(post("/api/folders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/folders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.message").value("Folder already exists!"));
    }

    @Test
    void testShouldReturnBadRequestWhenCreatingFolderWithBlankName() throws Exception {
        String request = """
            {
                "name": "   ",
                "parentId": null
            }
            """;

        mockMvc.perform(post("/api/folders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void testShouldReturnBadRequestWhenFolderNameExceedsNameLength() throws Exception {
        String nameTooLong = "a".repeat(300);

        String request = """
            {
                "name": "%s",
                "parentId": null
            }
            """.formatted(nameTooLong);

        mockMvc.perform(post("/api/folders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void testShouldReturnNotFoundWhenParentFolderDoesNotExist() throws Exception {
        String request = """
            {
                "name": "SomeRandomTestFolder",
                "parentId": 123456789
            }
            """;

        mockMvc.perform(post("/api/folders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

}
