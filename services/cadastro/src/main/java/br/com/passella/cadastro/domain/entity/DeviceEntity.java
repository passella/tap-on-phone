package br.com.passella.cadastro.domain.entity;

import java.util.List;

public class DeviceEntity {
    private String id;
    private String nome;

    private List<EstabelecimentoEntity> estabelecimentos;

    public DeviceEntity() {
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(final String nome) {
        this.nome = nome;
    }

    public List<EstabelecimentoEntity> getEstabelecimentos() {
        return estabelecimentos;
    }

    public void setEstabelecimentos(final List<EstabelecimentoEntity> estabelecimentos) {
        this.estabelecimentos = estabelecimentos;
    }
}
