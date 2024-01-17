package com.tech.bee.postservice.configuration.personalisation;

import com.tech.bee.postservice.configuration.client.BaseHttpClientProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "personalisation.client")
public class PersonalisationClientProperties extends BaseHttpClientProperties {
    private String fetchPreferencesPath;
}
