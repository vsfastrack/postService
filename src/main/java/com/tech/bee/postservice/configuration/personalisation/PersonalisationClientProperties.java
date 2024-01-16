package com.tech.bee.postservice.configuration.personalisation;

import com.tech.bee.postservice.configuration.client.BaseHttpClientProperties;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "personalisation.client")
public class PersonalisationClientProperties extends BaseHttpClientProperties {
    private String fetchPreferencesPath;
}
