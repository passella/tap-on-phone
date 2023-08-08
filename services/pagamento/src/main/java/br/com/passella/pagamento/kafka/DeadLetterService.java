package br.com.passella.pagamento.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.text.MessageFormat;
import java.util.UUID;

@Component
public class DeadLetterService extends AbstractKafkaProducerService {

    @Value(value = "${dead-letter.topic}")
    private String topic;

    public DeadLetterService(final KafkaTemplate<String, String> kafkaTemplate, final ObjectMapper mapper) {
        super(kafkaTemplate, mapper);
    }

    public void send(Throwable e) {
        logger.info("Enviando mensagem para dead letter");
        kafkaTemplate.send(topic, UUID.randomUUID().toString(), e.toString())
                .whenComplete((sendResult, throwable) -> {
                    if (throwable == null) {
                        logger.info(MessageFormat.format("Mensagem para dead letter enviada com sucesso: {0}", sendResult));
                    } else {
                        logger.info(MessageFormat.format("Falha ao enviar mensagem para o dead letter: {0}", throwable));
                    }
                });
        logger.info("Mensagem para dead letter enviada com sucesso");
    }

    public void send(final Mono<ResponseEntity<Object>> message) {
        message
                .flatMap(responseEntity -> {
                    final var body = responseEntity.getBody();
                    if (body == null) {
                        return Mono.empty();
                    }
                    try {
                        return Mono.just(mapper.writeValueAsString(body));
                    } catch (final JsonProcessingException e) {
                        return Mono.just(e.toString());
                    }
                })
                .subscribe(topicMessage -> executorService.submit(() -> {
                    logger.info("Enviando mensagem para dead letter");
                    kafkaTemplate.send(topic, UUID.randomUUID().toString(), topicMessage)
                            .whenComplete((sendResult, throwable) -> {
                                if (throwable == null) {
                                    logger.info(MessageFormat.format("Mensagem para dead letter enviada com sucesso: {0}", sendResult));
                                } else {
                                    logger.info(MessageFormat.format("Falha ao enviar mensagem para o dead letter: {0}", throwable));
                                }
                            });
                    logger.info("Mensagem para dead letter enviada com sucesso");
                }));
    }
}
