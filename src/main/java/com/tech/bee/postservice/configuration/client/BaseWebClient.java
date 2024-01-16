package com.tech.bee.postservice.configuration.client;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.util.concurrent.TimeUnit;

public class BaseWebClient {
    protected final WebClient client;

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
}
