package br.com.passella.motorpagamento.domain.model;

import java.time.LocalDateTime;

public record HistoricoPagamento(StatusPagamento statusPagamentoAntigo, StatusPagamento statusNovo, LocalDateTime dateTime) {
}
