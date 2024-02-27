package com.tech.bee.postservice.configuration.client;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "admin.cli")
public class AdminClientProperties {
    private String clientId;
    private String clientSecret;
    private String grantType;
    private String tokenUrl;
}
