package com.tech.bee.postservice.entity;

import com.tech.bee.postservice.enums.Enums;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
@Table(name = "reactions")
public class ReactionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "post_id")
    private String postId;
    @Column(name = "user_id")
    private String userId;
    @Enumerated(EnumType.STRING)
    private Enums.ReactionEnum reaction;
    private String reactionId = UUID.randomUUID().toString();
    @CreationTimestamp
    private LocalDateTime createdWhen;
    @UpdateTimestamp
    private LocalDateTime lastModifiedWhen;
}
