package com.tech.bee.postservice.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ExternalApiModel <T>{
    private LocalDateTime timestamp;
    private String transactionId;
    private T content;
}
