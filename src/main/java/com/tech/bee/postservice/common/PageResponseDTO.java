package com.tech.bee.postservice.common;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PageResponseDTO <P> {
    private Object results;
    private Long totalResults;
}
