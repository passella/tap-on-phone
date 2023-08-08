package br.com.passella.pagamento.domain.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.time.LocalDateTime;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record HistoricoPagamento(StatusPagamento statusPagamentoAntigo, StatusPagamento statusNovo, LocalDateTime dateTime) {
}
