package com.upgrad.patterns.Config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RestTemplate;

public class RestServiceGenerator {

    private static Logger logger = LoggerFactory.getLogger(RestServiceGenerator.class);

    private static RestTemplate restTemplate;

    private RestServiceGenerator() {
    }

    public static RestTemplate GetInstance() {
        // Execute if restTemplate is null
        // Initialize restTemplate. This is executed only once.
        if (restTemplate == null) {

            restTemplate = new RestTemplateBuilder()
                    .interceptors((request, body, execution) -> {
                        logger.info(String.format("Calling %s %s", request.getMethod(), request.getURI()));
                        ClientHttpResponse response = execution.execute(request, body);
                        logger.info("Call completed %s %s responded with %s", request.getMethod(), request.getURI(), response.getStatusCode());
                        return response;
                    })
                    .build();
        }

        return restTemplate;
    }
}
