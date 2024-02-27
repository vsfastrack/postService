package com.tech.bee.postservice.client;

import com.tech.bee.postservice.configuration.client.BaseHttpClientProperties;
import com.tech.bee.postservice.configuration.client.AdminClientProperties;
import com.tech.bee.postservice.dto.client.TokenDTO;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.util.concurrent.TimeUnit;

@Getter
public class BaseWebClient {
    protected final WebClient client;
    @Autowired
    protected AdminClientProperties adminClientProperties;

    public BaseWebClient(final BaseHttpClientProperties baseHttpClientProperties){
        HttpClient httpClient = HttpClient.create()
                .tcpConfiguration(client ->
                        client.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
                                .doOnConnected(conn -> conn
                                        .addHandler(new ReadTimeoutHandler(10, TimeUnit.SECONDS))
                                        .addHandlerLast(new WriteTimeoutHandler(10, TimeUnit.SECONDS))));

        ClientHttpConnector connector = new ReactorClientHttpConnector(httpClient.wiretap(true));
        this.client = WebClient.builder()
                .baseUrl(baseHttpClientProperties.getBaseUrl())
                .clientConnector(connector)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    protected String obtainAccessToken(){
        String requestBody = "grant_type=client_credentials" +
                "&client_id=" + adminClientProperties.getClientId()+
                "&client_secret=" + adminClientProperties.getClientSecret();

        // Make a POST request to the Keycloak token endpoint to obtain the access token
        return client.post()
                .uri(adminClientProperties.getTokenUrl())
                .header("Content-Type", "application/x-www-form-urlencoded")
                .body(BodyInserters.fromValue(requestBody))
                .retrieve()
                .bodyToMono(TokenDTO.class)
                .block()
                .getAccess_token();
    }
}
