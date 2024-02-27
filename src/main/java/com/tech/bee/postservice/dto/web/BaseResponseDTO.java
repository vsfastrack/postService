package com.tech.bee.postservice.dto.web;

import com.tech.bee.postservice.common.ErrorDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class BaseResponseDTO {
    private String timestamp;
    private String transactionId;
    private List<ErrorDTO> errors;
}
