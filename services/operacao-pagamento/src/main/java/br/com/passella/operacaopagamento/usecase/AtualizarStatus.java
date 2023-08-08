package br.com.passella.operacaopagamento.usecase;

import br.com.passella.operacaopagamento.domain.model.StatusPagamento;
import br.com.passella.operacaopagamento.service.PagamentoService;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
public class AtualizarStatus {

    private final PagamentoService pagamentoService;

    public AtualizarStatus(final PagamentoService pagamentoService) {
        this.pagamentoService = pagamentoService;
    }

    public Mono<AtualizarStatusOutput> execute(final AtualizarStatusInput input){
        final var atualizarStatusInput = new PagamentoService.AtualizarStatusInput(UUID.randomUUID().toString(), input.idPagamento(), input.statusPagamento());
        pagamentoService.update(atualizarStatusInput);
        return Mono.just(new AtualizarStatusOutput(atualizarStatusInput.id(), atualizarStatusInput.idPagamento(), atualizarStatusInput.statusPagamento()));
    }

    public record AtualizarStatusInput(String idPagamento, StatusPagamento statusPagamento) {
    }



    public record AtualizarStatusOutput(String id, String idPagamento, StatusPagamento statusPagamento) {
    }
}
