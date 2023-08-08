package br.com.passella.motorpagamento.error.handle.kafka;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.listener.RecordInterceptor;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.logging.Logger;

@Component
public class ApplicationKafkaRecordInterceptor implements RecordInterceptor<String, String> {
    private final Logger logger = Logger.getLogger(getClass().getName());


    @Override
    public ConsumerRecord<String, String> intercept(ConsumerRecord<String, String> record, Consumer<String, String> consumer) {
        return record;
    }

    @Override
    public void failure(ConsumerRecord<String, String> record, Exception exception, Consumer<String, String> consumer) {
        logger.severe(MessageFormat.format("Erro ao processar mensagem: {0}", record));
    }
}
