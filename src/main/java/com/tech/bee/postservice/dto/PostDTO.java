package com.tech.bee.postservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
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
}
