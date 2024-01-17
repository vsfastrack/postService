package com.tech.bee.postservice.repository;

import com.tech.bee.postservice.entity.PostEntity;
import com.tech.bee.postservice.model.PreferenceModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends PagingAndSortingRepository<PostEntity, Long>, JpaSpecificationExecutor<PostEntity> {

    Page<PostEntity> findAll(Specification<PostEntity> spec, Pageable pageable);
    Optional<PostEntity> findByIdentifier(String postIdentifier);
    Optional<PostEntity> findByPostId(String postIdentifier);

    @Query(value = "SELECT * FROM posts WHERE likes IS NOT NULL ORDER BY likes DESC LIMIT ?1", nativeQuery = true)
    List<PostEntity> findTopRatedPosts(int limit);

    @Query(value = "SELECT t.name as name ,p.title as title,p.post_id as postId,p.subtitle as subtitle,\n" +
            "p.description as description,p.likes as likes,p.read_minutes as readMinutes,\n" +
            "p.viewed_by as viewedBy\n" +
            "FROM posts p \n" +
            "JOIN post_tags pt ON pt.post_id = p.id \n" +
            "JOIN tags t ON t.id = pt.tag_id \n" +
            "WHERE t.identifier  = :tagIdentifier",
            nativeQuery = true)
    List<PreferenceModel> findPostsByTags(@Param("tagIdentifier") final String tagIdentifier);
}
