package com.tech.bee.postservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LinkDTO {
    private String linkId;
    private String identifier;
    private String link;
    private LocalDateTime createdWhen;
    private LocalDateTime lastModifiedWhen;
}
