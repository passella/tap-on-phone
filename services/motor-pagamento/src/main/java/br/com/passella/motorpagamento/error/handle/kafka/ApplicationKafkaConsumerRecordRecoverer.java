package br.com.passella.motorpagamento.error.handle.kafka;

import br.com.passella.motorpagamento.service.kafka.DeadLetterService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.context.annotation.Lazy;
import org.springframework.kafka.listener.ConsumerRecordRecoverer;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.logging.Logger;

@Component
public class ApplicationKafkaConsumerRecordRecoverer implements ConsumerRecordRecoverer {

    private  final Logger logger = Logger.getLogger(getClass().getName());
    private final DeadLetterService deadLetterService;

    public ApplicationKafkaConsumerRecordRecoverer(@Lazy final DeadLetterService deadLetterService) {
        this.deadLetterService = deadLetterService;
    }

    @Override
    public void accept(final ConsumerRecord<?, ?> consumerRecord, final Exception e) {
        logger.severe(MessageFormat.format("Excedeu todas as tentativas! {0}", e.toString()));
        deadLetterService.send(e);
    }
}
