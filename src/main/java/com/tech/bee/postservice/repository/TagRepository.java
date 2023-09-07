package com.tech.bee.postservice.repository;

import com.tech.bee.postservice.entity.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface TagRepository extends JpaRepository<TagEntity , Long>{
    Set<TagEntity> findByTagIdIn(List<String> tagId);
//    @Query("SELECT e FROM YourEntity e WHERE UPPER(e.name) = UPPER(:name)")
    Optional<TagEntity> findByNameIgnoreCase(@Param("name") String name);
}
