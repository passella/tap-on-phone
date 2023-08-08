package br.com.passella.motorpagamento.error.handle.kafka;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.support.ExponentialBackOffWithMaxRetries;
import org.springframework.stereotype.Component;

@Component
public class ApplicationKafkaBackOff extends ExponentialBackOffWithMaxRetries {

    public ApplicationKafkaBackOff(@Value(value = "${spring.kafka.retry.topic.attempts}") final int retryCount,
                                   @Value(value = "${spring.kafka.retry.topic.max-interval}") final long maxInterval,
                                   @Value(value = "${spring.kafka.retry.topic.multiplier}") final double multiplier,
                                   @Value(value = "${spring.kafka.retry.topic.initial}") final long initialInterval) {
        super(retryCount);
        setMaxInterval(maxInterval);
        setMultiplier(multiplier);
        setInitialInterval(initialInterval);
    }
}
