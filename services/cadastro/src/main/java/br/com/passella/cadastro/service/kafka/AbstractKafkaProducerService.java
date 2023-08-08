package br.com.passella.cadastro.service.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class AbstractKafkaProducerService {
    protected final KafkaTemplate<String, String> kafkaTemplate;
    protected final ObjectMapper mapper;
    protected final Logger logger = Logger.getLogger(getClass().getName());
    protected final ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public AbstractKafkaProducerService(final KafkaTemplate<String, String> kafkaTemplate, final ObjectMapper mapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.mapper = mapper;
    }
}
