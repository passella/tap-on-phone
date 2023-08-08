package br.com.passella.pagamento.repository;

import br.com.passella.pagamento.domain.entity.PagamentoEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface PagamentosRepository extends ReactiveMongoRepository<PagamentoEntity, String> {

    Flux<PagamentoEntity> findAllByDadosPagamentoDeviceEstabelecimentosId(String id);
}
