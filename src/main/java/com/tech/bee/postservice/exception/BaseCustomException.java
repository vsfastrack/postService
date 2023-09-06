package com.tech.bee.postservice.exception;

import com.tech.bee.postservice.common.ErrorDTO;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Builder
public class BaseCustomException extends RuntimeException{
    private HttpStatus httpStatus;
    private List<ErrorDTO> errors;
}
