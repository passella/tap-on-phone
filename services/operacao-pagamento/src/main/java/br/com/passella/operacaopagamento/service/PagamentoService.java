package br.com.passella.operacaopagamento.service;

import br.com.passella.operacaopagamento.domain.model.StatusPagamento;
import br.com.passella.operacaopagamento.provider.EfetuarPagamentoProvider;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

public interface PagamentoService {
    void send(EfetuarPagamentoInput pagamento);

    void update(AtualizarStatusInput atualizarStatusInput);

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    record EfetuarPagamentoInput(EfetuarPagamentoProvider.DadosPagamento device, String idPagamento, StatusPagamento status) {
    }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    record AtualizarStatusInput(String id, String idPagamento, StatusPagamento statusPagamento) {
    }
}
