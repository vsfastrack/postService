package com.tech.bee.postservice.configuration.client;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BaseHttpClientProperties {
    private String baseUrl;
    private Long readTimeOut;
    private Long connectTimeout;
}
