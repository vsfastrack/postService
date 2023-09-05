package com.tech.bee.postservice.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tech.bee.postservice.enums.Enums;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorDTO {
    private String code;
    private String message;
    private String key;
    private Enums.ErrorCategory category;
    private Enums.ErrorStatus status;
}
