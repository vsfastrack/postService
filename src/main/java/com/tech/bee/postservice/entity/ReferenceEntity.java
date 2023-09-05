package com.tech.bee.postservice.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.reflect.MethodDelegate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity
@Table(name = "references")
public class ReferenceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String referenceId;
    private String type;
    private String content;
    @ManyToMany(mappedBy = "posts")
    private Set<PostEntity> posts = new HashSet<>();
    private String createdBy;
    private LocalDateTime createdWhen;
}
