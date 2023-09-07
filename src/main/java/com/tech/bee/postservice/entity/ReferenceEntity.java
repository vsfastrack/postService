package com.tech.bee.postservice.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
@Table(name = "references")
public class ReferenceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "reference_id")
    private String referenceId;
    private String identifier = UUID.randomUUID().toString();
    private String content;
    @ManyToOne
    @JoinColumn(referencedColumnName = "post_id")
    private PostEntity post;
    private String createdBy;
    @CreatedDate
    private LocalDateTime createdWhen;
    @LastModifiedDate
    private LocalDateTime lastModifiedWhen;
}
