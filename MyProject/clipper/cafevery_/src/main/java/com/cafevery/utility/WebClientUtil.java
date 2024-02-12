package com.cafevery.utility;


import com.cafevery.config.WebClientConfig;
import com.cafevery.dto.type.ErrorCode;
import com.cafevery.exception.CommonException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebClientUtil {

    private final WebClientConfig webClientConfig;

    public <T> T get(String url, Class<T> responseDtoClass, Map<String, String> headers, Map<String, String> queryParams) {
        log.info("url: {}", url);
        WebClient.RequestHeadersSpec<?> requestSpec = webClientConfig.webClient()
                .method(HttpMethod.GET)
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .host("dapi.kakao.com")
                        .pathSegment("v2", "local", "search", "keyword.json")
                        .queryParam("query", queryParams.get("query"))
                        .queryParam("x", queryParams.get("x"))
                        .queryParam("y", queryParams.get("y"))
                        .build());
        if (headers != null) {
            headers.forEach(requestSpec::header);
        }
        return requestSpec
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse ->
                        Mono.error(new CommonException(ErrorCode.MISSING_REQUEST_PARAMETER))
                )
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse ->
                        Mono.error(new CommonException(ErrorCode.INTERNAL_SERVER_ERROR))
                )
                .bodyToMono(responseDtoClass)
                .block();
    }
}