package com.tech.bee.postservice.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "posts")
public class PostEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "post_id")
    private String postId;
    private String identifier = UUID.randomUUID().toString();
    private String title;
    private String subtitle;
    private String category;
    private String series;
    private String link;
    @Type(type = "text")
    @Column(columnDefinition = "text")
    private String content;
    @Type(type = "text")
    @Column(columnDefinition = "text")
    private String description;
    private String authorId;
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "post_tags",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<TagEntity> tags = new HashSet<>();
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "post_id")
    private List<LinkEntity> links = new ArrayList<>();
    private Long likes;
    @Column(name = "viewed_by")
    private Long viewedBy;
    @Column(name="read_minutes")
    private Long readMinutes;
    @Column(name = "cover_image")
    private String coverImage;
    private String createdBy;
    @CreationTimestamp
    private LocalDateTime createdWhen;
    @UpdateTimestamp
    private LocalDateTime lastModifiedWhen;
}
