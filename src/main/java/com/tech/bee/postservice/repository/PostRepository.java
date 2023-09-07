package com.tech.bee.postservice.repository;

import com.tech.bee.postservice.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<PostEntity , Long>{
}
