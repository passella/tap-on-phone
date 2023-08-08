package br.com.passella.motorpagamento.entrypoint.kafka;

import br.com.passella.motorpagamento.usecase.CriarPagamento;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.logging.Logger;

@Component
public class PagamentoEfetuadoListener {

    protected final Logger logger = Logger.getLogger(getClass().getName());
    private final CriarPagamento criarPagamento;
    private final ObjectMapper mapper;

    public PagamentoEfetuadoListener(final CriarPagamento criarPagamento, final ObjectMapper mapper) {
        this.criarPagamento = criarPagamento;
        this.mapper = mapper;
    }

    @KafkaListener(topics = "${pagamento-efetuado.topic}", groupId = "${spring.kafka.consumer.group-id}", containerFactory = "kafkaListenerContainerFactory")
    public void efetuarPagamento(final Message<String> message) throws JsonProcessingException {
        final var criarPagamentoInput = mapper.readValue(message.getPayload(), CriarPagamento.CriarPagamentoInput.class);
        logger.info(MessageFormat.format("Mensagem recebida: {0}", criarPagamentoInput));
        final var pagamentoOutput = criarPagamento.execute(criarPagamentoInput);
        logger.info(MessageFormat.format("Pagamento processado: {0}", pagamentoOutput));
    }
}
