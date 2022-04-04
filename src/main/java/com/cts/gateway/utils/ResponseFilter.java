package com.cts.gateway.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class ResponseFilter {
    private final FilterUtils filterUtils;

    @Bean
    public GlobalFilter postGlobalFilter() {
        return ((exchange, chain) -> {
            return chain.filter(exchange)
                        .then(Mono.fromRunnable(() -> {
                            HttpHeaders httpHeaders = exchange.getRequest()
                                                              .getHeaders();
                            String correlationId = filterUtils.getCorrelationId(httpHeaders);

                            log.debug("Adding correlationId {} to outbound headers", correlationId);
                            exchange.getResponse()
                                    .getHeaders()
                                    .add(FilterUtils.CORRELATION_ID, correlationId);
                            log.debug("Completing outgoing request for {}", exchange.getRequest()
                                                                                    .getURI());
                        }));
        });
    }
}
