package com.tech.bee.postservice.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
@Table(name = "posts")
public class PostEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String postId;
    private String identifier = UUID.randomUUID().toString();
    private String title;
    private String subtitle;
    private String category;
    private String link;
    private String content;
    private String authorId;
    @ManyToMany
    @JoinTable(name = "post_tags",joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<TagEntity>  tags = new HashSet<>();
    @OneToMany(mappedBy = "post")
    private Set<ReferenceEntity> references = new HashSet<>();
    @CreatedDate
    private LocalDateTime createdWhen;
    @LastModifiedDate
    private LocalDateTime lastModifiedWhen;
}
