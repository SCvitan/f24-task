package com.example.backend.repository;

import com.example.backend.entity.Folder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FolderRepository extends JpaRepository<Folder, Long> {

    List<Folder> findByParentId(Long parentId);
    List<Folder> findByParentIsNull();
    boolean existsByParentIdAndName(Long parentId, String name);
    boolean existsByParentIsNullAndName(String name);

}
