package com.tech.bee.postservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PreferenceDTO {
    private String preference;
    private List<PostSummaryDTO> postSummaries;
}
