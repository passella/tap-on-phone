package br.com.passella.cadastro.entrypoint.controller;

import br.com.passella.cadastro.config.RateLimiterProperties;
import io.github.resilience4j.bulkhead.Bulkhead;
import io.github.resilience4j.bulkhead.BulkheadConfig;
import io.github.resilience4j.bulkhead.BulkheadRegistry;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.reactor.bulkhead.operator.BulkheadOperator;
import io.github.resilience4j.reactor.ratelimiter.operator.RateLimiterOperator;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;


public abstract class AbstractController {

    private final RateLimiter rateLimiter;
    private final Bulkhead bulkhead;

    protected AbstractController(final RateLimiterProperties rateLimiterProperties) {
        rateLimiter = RateLimiter.of("rate-limiter", RateLimiterConfig.custom()
                .limitRefreshPeriod(Duration.ofSeconds(rateLimiterProperties.getLimitRefreshPeriod()))
                .limitForPeriod(rateLimiterProperties.getLimitForPeriod())
                .timeoutDuration(Duration.ofSeconds(rateLimiterProperties.getTimeoutDuration()))
                .build());

        final var bulkheadConfig = BulkheadConfig.custom()
                .maxConcurrentCalls(rateLimiterProperties.getMaxConcurrentCalls())
                .build();

        bulkhead = BulkheadRegistry.of(bulkheadConfig).bulkhead("bulkhead-limiter");
    }


    protected static <T> Mono<T> flatMapRetry(final Mono<T> execute) {
        return execute
                .retryWhen(Retry.backoff(1, Duration.ofSeconds(1)).jitter(0.75));
    }

    protected <T> Mono<T> monoLimiter(final Mono<T> mono) {
        return mono
                .transformDeferred(RateLimiterOperator.of(rateLimiter))
                .transformDeferred(BulkheadOperator.of(bulkhead));
    }
}
