package com.example.backend.repository;

import com.example.backend.entity.FileEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<FileEntity, Long> {

    List<FileEntity> findByFolderId(Long folderId);
    boolean existsByFolderIdAndName(Long folderId, String name);

    @Query("""
            SELECT f FROM FileEntity f
            WHERE f.name LIKE CONCAT(:query, '%')
            ORDER BY f.name
            """)
    List<FileEntity> searchByName(@Param("query") String query, Pageable pageable);

    @Query("""
            SELECT f FROM FileEntity f
            WHERE f.folder.id = :folderId AND f.name LIKE CONCAT(:query, '%')
            ORDER BY f.name
            """)
    List<FileEntity> searchByNameInFolder(@Param("query") String query, @Param("folderId") Long folderId, Pageable pageable);

    List<FileEntity> findByName(String name);

    List<FileEntity> findByFolderIdAndName(Long folderId, String name);

}
