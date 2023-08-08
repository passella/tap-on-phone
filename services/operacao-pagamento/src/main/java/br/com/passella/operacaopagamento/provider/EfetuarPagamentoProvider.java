package br.com.passella.operacaopagamento.provider;

import br.com.passella.operacaopagamento.domain.model.StatusPagamento;
import br.com.passella.operacaopagamento.provider.impl.CadastroOpenFeignClient.DeviceResponse;
import br.com.passella.operacaopagamento.service.PagamentoService;
import br.com.passella.operacaopagamento.usecase.EfetuarPagamento.EfetuarPagamentoInput;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
public class EfetuarPagamentoProvider {

    private final PagamentoService pagamentoService;

    public EfetuarPagamentoProvider(final PagamentoService pagamentoService) {
        this.pagamentoService = pagamentoService;
    }

    public Mono<PagamentoEfetuado> send(final DadosPagamento pagamento) {
        final var pagamentoInput = new PagamentoService.EfetuarPagamentoInput(pagamento, UUID.randomUUID().toString(), StatusPagamento.PENDENTE);
        pagamentoService.send(pagamentoInput);
        PagamentoEfetuado pagamentoEfetuado = new PagamentoEfetuado(pagamento, pagamentoInput.idPagamento(), pagamentoInput.status());
        return Mono.just(pagamentoEfetuado);
    }

    public record DadosPagamento(DeviceResponse device, EfetuarPagamentoInput pagamento) {
    }

    public record PagamentoEfetuado(EfetuarPagamentoProvider.DadosPagamento device, String idPagamento, StatusPagamento status) {
    }


}
