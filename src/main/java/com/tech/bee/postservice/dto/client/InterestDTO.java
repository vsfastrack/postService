package com.tech.bee.postservice.dto.client;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tech.bee.postservice.dto.web.BaseResponseDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InterestDTO extends BaseResponseDTO {
    private List<String> content;
}
