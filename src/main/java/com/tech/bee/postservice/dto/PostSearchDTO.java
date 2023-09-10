package com.tech.bee.postservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tech.bee.postservice.common.BaseSearchDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostSearchDTO extends BaseSearchDTO {
    private String title;
    private String subtitle;
    private List<String> tags;
    private String category;
    private String series;
    private String authorId;
    private String createdWhenFrom;
    private String createdWhenTo;
}
