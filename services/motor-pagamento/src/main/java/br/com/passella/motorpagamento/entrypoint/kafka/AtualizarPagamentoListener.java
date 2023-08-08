package br.com.passella.motorpagamento.entrypoint.kafka;

import br.com.passella.motorpagamento.domain.model.AtualizarStatusInput;
import br.com.passella.motorpagamento.error.exception.PagamentosRepositoryNotFountException;
import br.com.passella.motorpagamento.usecase.AtualizarPagamento;
import br.com.passella.motorpagamento.usecase.converter.AtualizarPagamentoConverter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.logging.Logger;

@Component
public class AtualizarPagamentoListener {

    protected final Logger logger = Logger.getLogger(getClass().getName());
    private final AtualizarPagamento atualizarPagamento;
    private final AtualizarPagamentoConverter atualizarPagamentoConverter;
    private final ObjectMapper mapper;

    public AtualizarPagamentoListener(final AtualizarPagamento atualizarPagamento, final AtualizarPagamentoConverter atualizarPagamentoConverter, final ObjectMapper mapper) {
        this.atualizarPagamento = atualizarPagamento;
        this.atualizarPagamentoConverter = atualizarPagamentoConverter;
        this.mapper = mapper;
    }

    @KafkaListener(topics = "${pagamento-atualizado.topic}", groupId = "${spring.kafka.consumer.group-id}", containerFactory = "kafkaListenerContainerFactory")
    public void atualizarPagamento(final Message<String> message) throws JsonProcessingException, PagamentosRepositoryNotFountException {
        final var atualizarStatusInput = mapper.readValue(message.getPayload(), AtualizarStatusInput.class);
        logger.info(MessageFormat.format("Mensagem recebida: {0}", atualizarStatusInput));
        final var input = atualizarPagamentoConverter.toInput(atualizarStatusInput);
        final var atualizarPagamentoOutput = atualizarPagamento.execute(input);
        logger.info(MessageFormat.format("Pagamento processado: {0}", atualizarPagamentoOutput));
    }

}
