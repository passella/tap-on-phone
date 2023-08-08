package br.com.passella.cadastro.domain.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;


public class DeviceRequest {

    @NotNull(message = "Nome é obrigatório")
    private String nome;
    @NotNull(message = "Lista de estabelecimentos é obrigatória")
    @NotEmpty(message = "Lista de estabelecimentos não pode estar vazia")
    private List<@NotEmpty(message = "ID do estabelecimento é obrigatório") String> estabelecimentos;

    public DeviceRequest(final String nome, final List<String> estabelecimentos) {
        this.nome = nome;
        this.estabelecimentos = estabelecimentos;
    }

    public DeviceRequest() {
    }

    public String getNome() {
        return nome;
    }

    public List<String> getEstabelecimentos() {
        return estabelecimentos;
    }
}
