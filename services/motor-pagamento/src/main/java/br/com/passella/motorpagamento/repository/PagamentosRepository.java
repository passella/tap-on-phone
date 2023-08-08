package br.com.passella.motorpagamento.repository;

import br.com.passella.motorpagamento.domain.entity.PagamentoEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PagamentosRepository extends MongoRepository<PagamentoEntity, String> {
    @Override
    Optional<PagamentoEntity> findById(String s);
}
