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
    Set<TagEntity> findAllByTagIdIn(List<String> tagId);
    @Query("SELECT t FROM TagEntity t WHERE UPPER(t.name) = UPPER(:name)")
    Optional<TagEntity> findByNameIgnoreCase(@Param("name") String name);
    List<TagEntity> findAllByIdentifierIn(List<String> tagIds);
    List<TagEntity> findAllByNameIn(List<String> tagNames);

    Optional<TagEntity> findByIdentifier(String tagIdentifier);
}
