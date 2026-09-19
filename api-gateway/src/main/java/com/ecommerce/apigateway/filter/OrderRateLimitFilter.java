package com.ecommerce.apigateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class OrderRateLimitFilter implements GlobalFilter, Ordered {

    private static final int MAX_REQUESTS = 5;
    private static final long WINDOW_SECONDS = 60;

    private record RequestCounter(
            long windowStart,
            int count
    ) {
    }

    @Override
    public int getOrder() {
        return -1;
    }

    private final Map<String, RequestCounter> requestCounters =
            new ConcurrentHashMap<>();

    public Mono<Void> filter(
            ServerWebExchange exchange,
            GatewayFilterChain chain
    ){
        String path = exchange.getRequest().getPath().value();
        HttpMethod method = exchange.getRequest().getMethod();

        if (!HttpMethod.POST.equals(method)
                || !"/api/v1/orders".equals(path)) {

            return chain.filter(exchange);
        }

        String clientIp =
                exchange.getRequest()
                        .getRemoteAddress()
                        .getAddress()
                        .getHostAddress();

        long now = Instant.now().getEpochSecond();

        RequestCounter counter =
                requestCounters.compute(
                        clientIp,
                        (key, existing) -> {

                            if (existing == null
                                    || now - existing.windowStart()
                                    >= WINDOW_SECONDS) {

                                return new RequestCounter(now, 1);
                            }

                            return new RequestCounter(
                                    existing.windowStart(),
                                    existing.count() + 1
                            );
                        });

        if (counter.count() > MAX_REQUESTS) {

            exchange.getResponse()
                    .setStatusCode(HttpStatus.TOO_MANY_REQUESTS);

            return exchange.getResponse().setComplete();
        }

        return chain.filter(exchange);

    }
}
