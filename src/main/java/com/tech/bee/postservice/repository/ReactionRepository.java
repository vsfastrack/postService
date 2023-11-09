package com.tech.bee.postservice.repository;

import com.tech.bee.postservice.entity.ReactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReactionRepository extends JpaRepository<ReactionEntity , Long> {
    Optional<ReactionEntity> findByPostId(String postId);
    ReactionEntity findByPostIdAndUserId(String postId , String userId);
}
