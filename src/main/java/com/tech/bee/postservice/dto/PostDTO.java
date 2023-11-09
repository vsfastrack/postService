package com.tech.bee.postservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostDTO {
    private String title;
    private String subtitle;
    private String description;
    private String series;
    private String category;
    private String content;
    private List<TagDTO> tags;
    private String authorId;
    private String postId;
    private String identifier;
    private String link;
    private List<String> links;
    private Long minToRead;
    private Long viewedBy;
    private String likesCount;
    private String coverImage;
    private String publishedOn;
    private boolean isLiked;
    private boolean isAuthor;
}
