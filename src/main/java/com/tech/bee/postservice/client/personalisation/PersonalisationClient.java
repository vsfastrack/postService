package com.tech.bee.postservice.client.personalisation;

import com.tech.bee.postservice.client.BaseWebClient;
import com.tech.bee.postservice.configuration.personalisation.PersonalisationClientConfiguration;
import com.tech.bee.postservice.constants.ApiConstants;
import com.tech.bee.postservice.dto.client.InterestDTO;
import com.tech.bee.postservice.exception.BaseCustomException;
import com.tech.bee.postservice.service.SecurityService;
import com.tech.bee.postservice.util.AppUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriTemplate;

import java.util.Collections;
import java.util.List;

@Component
public class PersonalisationClient extends BaseWebClient {
    private final SecurityService securityService;
    private final PersonalisationClientConfiguration configuration;

    @Autowired
    PersonalisationClient(PersonalisationClientConfiguration clientConfiguration,
                          final SecurityService securityService) {
        super(clientConfiguration.getPersonalisationClientProperties());
        this.securityService = securityService;
        this.configuration = clientConfiguration;
    }

    public List<String> fetchInterests(final String userId) {
        final String fetchPreferencesUri = configuration.getPersonalisationClientProperties().getFetchPreferencesPath() + userId;
        InterestDTO interestResponse = client.get().uri(new UriTemplate(fetchPreferencesUri).toString())
                .headers(httpHeaders -> httpHeaders.setContentType(MediaType.APPLICATION_JSON))
                .headers(httpHeaders -> httpHeaders.setBearerAuth(obtainAccessToken()))
                .retrieve()
                .toEntity(InterestDTO.class)
                .blockOptional()
                .map(ResponseEntity::getBody).orElseThrow(() -> BaseCustomException.builder().
                        errors(Collections.singletonList(AppUtil.buildResourceNotFoundError(
                                ApiConstants.KeyConstants.KEY_POST))).httpStatus(HttpStatus.NOT_FOUND)
                        .build());
        return interestResponse.getContent();
    }

}
