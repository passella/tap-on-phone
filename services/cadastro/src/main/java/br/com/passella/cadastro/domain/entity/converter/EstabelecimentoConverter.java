package br.com.passella.cadastro.domain.entity.converter;

import br.com.passella.cadastro.domain.entity.EstabelecimentoEntity;
import br.com.passella.cadastro.usecase.CadastrarEstabelecimento.CadastrarEstabelecimentoOutput;
import org.springframework.stereotype.Component;

@Component
public class EstabelecimentoConverter {
    public CadastrarEstabelecimentoOutput toOutput(EstabelecimentoEntity entity) {
        return new CadastrarEstabelecimentoOutput(entity.getId(), entity.getNome());
    }
}
