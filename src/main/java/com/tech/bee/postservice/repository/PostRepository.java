package com.tech.bee.postservice.repository;

import com.tech.bee.postservice.entity.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
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
}
