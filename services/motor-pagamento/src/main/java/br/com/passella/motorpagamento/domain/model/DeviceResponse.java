package br.com.passella.motorpagamento.domain.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
record DeviceResponse(String id, String nome, List<Estabelecimento> estabelecimentos) {
}
