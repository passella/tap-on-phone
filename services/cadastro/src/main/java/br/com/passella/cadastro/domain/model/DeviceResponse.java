package br.com.passella.cadastro.domain.model;

import br.com.passella.cadastro.usecase.CadastrarEstabelecimento.CadastrarEstabelecimentoOutput;

import java.util.List;

public record DeviceResponse(String id, String nome, List<CadastrarEstabelecimentoOutput> estabelecimentos) {
}
