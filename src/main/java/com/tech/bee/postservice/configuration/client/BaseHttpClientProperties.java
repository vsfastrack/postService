package com.tech.bee.postservice.configuration.client;

import lombok.Data;
import lombok.Getter;

@Getter
public class BaseHttpClientProperties {
    private String baseUrl;
    private Long readTimeOut;
    private Long connectTimeout;
}
