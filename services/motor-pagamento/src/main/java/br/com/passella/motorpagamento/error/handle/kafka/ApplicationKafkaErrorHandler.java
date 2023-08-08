package br.com.passella.motorpagamento.error.handle.kafka;

import br.com.passella.motorpagamento.service.kafka.DeadLetterService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.kafka.listener.ConsumerRecordRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.ExponentialBackOffWithMaxRetries;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.logging.Logger;

@Component
public class ApplicationKafkaErrorHandler extends DefaultErrorHandler {
    private static final Logger logger = Logger.getLogger(ApplicationKafkaErrorHandler.class.getName());

    private final DeadLetterService deadLetterService;

    public ApplicationKafkaErrorHandler(@Value(value = "${spring.kafka.retry.topic.attempts}") final int retryCount,
                                        @Value(value = "${spring.kafka.retry.topic.max-interval}") final long maxInterval,
                                        @Value(value = "${spring.kafka.retry.topic.multiplier}") final double multiplier,
                                        @Lazy final DeadLetterService deadLetterService) {
        super(getConsumerRecordRecoverer(), getBackOff(retryCount, maxInterval, multiplier));
        this.deadLetterService = deadLetterService;
    }

    private static ExponentialBackOffWithMaxRetries getBackOff(final Integer retryCount, final long maxInterval, final double multiplier) {
        final var exponentialBackOffWithMaxRetries = new ExponentialBackOffWithMaxRetries(retryCount);
        exponentialBackOffWithMaxRetries.setMaxInterval(maxInterval);
        exponentialBackOffWithMaxRetries.setMultiplier(multiplier);
        return exponentialBackOffWithMaxRetries;
    }

    private static ConsumerRecordRecoverer getConsumerRecordRecoverer() {
        return (consumerRecord, e) -> {
            logger.severe(MessageFormat.format("Excedeu todas as tentativas! {0}", e.toString()));
        };
    }


}
