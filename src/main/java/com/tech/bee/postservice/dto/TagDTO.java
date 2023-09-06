package com.tech.bee.postservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TagDTO {
    private String tagId;
    private String identifier;
    private String name;
    private String createdBy;
    private LocalDateTime createdWhen;
    private LocalDateTime lastModified;
}
