package com.tech.bee.postservice.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponseDTO {
    private String timeStamp;
    private String transactionId;
    private Object content;
    private List<ErrorDTO> errors;
}
