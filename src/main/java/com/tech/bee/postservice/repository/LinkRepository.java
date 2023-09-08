package com.tech.bee.postservice.repository;

import com.tech.bee.postservice.entity.LinkEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LinkRepository extends JpaRepository<LinkEntity , Long> {
}
