package br.com.passella.cadastro.repository;

import br.com.passella.cadastro.domain.entity.EstabelecimentoEntity;
import br.com.passella.cadastro.error.exception.RepositoryException;

public interface EstabelecimentoRepository {
    EstabelecimentoEntity save(EstabelecimentoEntity cadastrarEstabelecimentoInput) throws RepositoryException;
}
