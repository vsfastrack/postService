package com.tech.bee.postservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReferenceDTO {
    private String name;
    private String referenceId;
    private String type;
    private String content;
}
