package com.tech.bee.postservice.configuration.personalisation;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@RequiredArgsConstructor
public class PersonalisationClientConfiguration {
    private final PersonalisationClientProperties personalisationClientProperties;
}
