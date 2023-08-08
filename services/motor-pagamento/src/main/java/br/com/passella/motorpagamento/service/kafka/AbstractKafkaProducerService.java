package br.com.passella.motorpagamento.service.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.logging.Logger;

public class AbstractKafkaProducerService {
    protected final KafkaTemplate<String, String> kafkaTemplate;
    protected final ObjectMapper mapper;
    protected final Logger logger = Logger.getLogger(getClass().getName());
    

    public AbstractKafkaProducerService(final KafkaTemplate<String, String> kafkaTemplate, final ObjectMapper mapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.mapper = mapper;
    }
}
