package br.com.passella.operacaopagamento.service.kafka;


import br.com.passella.operacaopagamento.service.PagamentoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

@Service
public class KafkaPagamentoService extends AbstractKafkaProducerService implements PagamentoService {

    @Value(value = "${pagamento-efetuado.topic}")
    private String topicPagar;

    @Value(value = "${pagamento-atualizado.topic}")
    private String topicAtualizar;

    public KafkaPagamentoService(final KafkaTemplate<String, String> kafkaTemplate, final ObjectMapper mapper) {
        super(kafkaTemplate, mapper);
    }

    @Override
    public void send(final EfetuarPagamentoInput pagamento) {
        logger.info(MessageFormat.format("Enviando pagamento efetuado para armazenamento: {0}", pagamento.idPagamento()));
        executorService.submit(() -> kafkaTemplate.send(topicPagar, mapper.writeValueAsString(pagamento)).whenComplete((sendResult, throwable) -> {
            if (throwable == null) {
                logger.info(MessageFormat.format("Pagamento efetuado enviado com sucesso: {0}", sendResult));
            } else {
                logger.info(MessageFormat.format("Falha ao enviar mensagem de pagamento efetuado: {0}", throwable));
            }
        }));
        logger.info(MessageFormat.format("Pagamento efetuado enviado para armazenado: {0}", pagamento.idPagamento()));

    }

    @Override
    public void update(AtualizarStatusInput atualizarStatusInput) {
        logger.info(MessageFormat.format("Enviando atualização de pagamento para armazenamento: {0}", atualizarStatusInput.id()));
        executorService.submit(() -> kafkaTemplate.send(topicAtualizar, mapper.writeValueAsString(atualizarStatusInput))
                .whenComplete((sendResult, throwable) -> {
                    if (throwable == null) {
                        logger.info(MessageFormat.format("Atualização de pagamento enviada com sucesso: {0}", sendResult));
                    } else {
                        logger.info(MessageFormat.format("Falha ao enviar mensagem de atualização de pagamento: {0}", throwable));
                    }
                }));
        logger.info(MessageFormat.format("Atualização de pagamento enviada para armazenamento: {0}", atualizarStatusInput.id()));
    }
}
