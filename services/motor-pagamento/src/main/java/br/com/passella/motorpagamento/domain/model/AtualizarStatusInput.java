package br.com.passella.motorpagamento.domain.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record AtualizarStatusInput(String id, String idPagamento, StatusPagamento statusPagamento) {
}
