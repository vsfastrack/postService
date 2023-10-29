package com.tech.bee.postservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class PostSummaryDTO {
    private String title;
    private String subtitle;
    private String description;
    private Long likesCount;
    private Long minToRead;
    private Long viewedBy;
    private String coverImage;
    private String postId;
    private List<String> tagNames;
}
