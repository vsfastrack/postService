package com.tech.bee.postservice.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity
@Table(name = "tags")
public class TagEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String tagId;
    private String name;
    @ManyToMany(mappedBy = "posts")
    private Set<PostEntity> posts = new HashSet<>();
    private String createdBy;
    private LocalDateTime createdWhen;
}
