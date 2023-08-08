package br.com.passella.pagamento.usecase;

import br.com.passella.pagamento.domain.model.DadosPagamento;
import br.com.passella.pagamento.domain.model.HistoricoPagamento;
import br.com.passella.pagamento.domain.model.StatusPagamento;
import br.com.passella.pagamento.error.exception.MongoDBNotFoundException;
import br.com.passella.pagamento.repository.PagamentosRepository;
import br.com.passella.pagamento.usecase.converter.ObterPagamentosConverter;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class ObterPagamentos {
    
    private final PagamentosRepository pagamentosRepository;
    private final ObterPagamentosConverter obterPagamentosConverter;

    public ObterPagamentos(final PagamentosRepository pagamentosRepository, final ObterPagamentosConverter obterPagamentosConverter) {
        this.pagamentosRepository = pagamentosRepository;
        this.obterPagamentosConverter = obterPagamentosConverter;
    }

    public Flux<ObterPagamentosOutput> execute(final ObterPagamentosInput input){
        return pagamentosRepository
                .findAllByDadosPagamentoDeviceEstabelecimentosId(input.id())
                .flatMap(pagamentoEntity -> Mono.just(obterPagamentosConverter.toOutput(pagamentoEntity)))
                .switchIfEmpty(Mono.error(new MongoDBNotFoundException("Não foram encontrados pagamentos para o estabelecimento: " + input.id())));
    }
    
    public record ObterPagamentosInput(String id){}
    public record ObterPagamentosOutput(
            String idPagamento,
            DadosPagamento dadosPagamento,
            StatusPagamento statusPagamento,
            List<HistoricoPagamento> historico
    ){}
}
