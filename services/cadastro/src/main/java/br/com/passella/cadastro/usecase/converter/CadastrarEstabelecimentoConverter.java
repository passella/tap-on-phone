package br.com.passella.cadastro.usecase.converter;

import br.com.passella.cadastro.domain.entity.EstabelecimentoEntity;
import br.com.passella.cadastro.domain.model.EstabelecimentoRequest;
import br.com.passella.cadastro.usecase.CadastrarEstabelecimento.CadastrarEstabelecimentoInput;
import br.com.passella.cadastro.usecase.CadastrarEstabelecimento.CadastrarEstabelecimentoOutput;
import org.springframework.stereotype.Component;

@Component
public class CadastrarEstabelecimentoConverter {
    public CadastrarEstabelecimentoInput toInput(final EstabelecimentoRequest estabelecimento) {
        return new CadastrarEstabelecimentoInput(estabelecimento.getNome());
    }

    public EstabelecimentoEntity toEntity(final CadastrarEstabelecimentoInput cadastrarEstabelecimentoInput) {
        final var estabelecimentoEntity = new EstabelecimentoEntity();
        estabelecimentoEntity.setNome(cadastrarEstabelecimentoInput.nome());
        return estabelecimentoEntity;
    }

    public CadastrarEstabelecimentoOutput toOutput(final EstabelecimentoEntity estabelecimentoEntity) {
        return new CadastrarEstabelecimentoOutput(estabelecimentoEntity.getId(), estabelecimentoEntity.getNome());
    }
}
