package br.com.passella.pagamento.domain.model;

import java.util.List;

record DeviceResponse(String id, String nome, List<Estabelecimento> estabelecimentos) {
}
