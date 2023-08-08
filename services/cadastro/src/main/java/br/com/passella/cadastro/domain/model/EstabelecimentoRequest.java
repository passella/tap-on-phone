package br.com.passella.cadastro.domain.model;

import jakarta.validation.constraints.NotNull;


public class EstabelecimentoRequest {

    @NotNull(message = "Nome é obrigatório")
    private String nome;

    public EstabelecimentoRequest() {
    }

    public EstabelecimentoRequest(final String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }
}
