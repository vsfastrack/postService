package com.tech.bee.postservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostDTO {
    private String title;
    private String subtitle;
    private String link;
    private String content;
    private String category;
    private String series;
    private String authorId;
    private List<TagDTO> tags;
    private List<String> links;
}
