package br.com.passella.cadastro.service.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class BigDataService extends AbstractKafkaProducerService{

    @Value(value = "${big-data.topic}")
    private String topic;

    private final DeadLetterService deadLetterService;
    public BigDataService(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper mapper, final DeadLetterService deadLetterService) {
        super(kafkaTemplate, mapper);
        this.deadLetterService = deadLetterService;
    }

    public <T> void send(T data) {
        executorService.submit(() -> {
            logger.info("Enviado registro para big data");
            try {
                kafkaTemplate.send(topic,mapper.writeValueAsString(data));
            } catch (JsonProcessingException e) {
                deadLetterService.send(e);
            }
            logger.info("Registro enviado para big data com sucesso");
        });

    }
}
