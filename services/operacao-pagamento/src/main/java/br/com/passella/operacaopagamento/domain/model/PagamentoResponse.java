package br.com.passella.operacaopagamento.domain.model;

import br.com.passella.operacaopagamento.provider.EfetuarPagamentoProvider.DadosPagamento;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record PagamentoResponse(DadosPagamento dadosPagamento, String idPagamento, StatusPagamento status) {

}
