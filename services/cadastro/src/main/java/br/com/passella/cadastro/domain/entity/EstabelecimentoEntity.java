package br.com.passella.cadastro.domain.entity;

public class EstabelecimentoEntity {
    private String id;
    private String nome;

    public EstabelecimentoEntity() {
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
}
