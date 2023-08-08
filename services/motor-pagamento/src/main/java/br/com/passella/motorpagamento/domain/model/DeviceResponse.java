package br.com.passella.motorpagamento.domain.model;

import java.util.List;

record DeviceResponse(String id, String nome, List<Estabelecimento> estabelecimentos) {
}
