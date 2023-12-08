package br.com.passella.operacaopagamento.usecase;

import br.com.passella.operacaopagamento.domain.model.StatusPagamento;
import br.com.passella.operacaopagamento.provider.CadastroProvider;
import br.com.passella.operacaopagamento.provider.EfetuarPagamentoProvider;
import br.com.passella.operacaopagamento.provider.EfetuarPagamentoProvider.DadosPagamento;
import br.com.passella.operacaopagamento.provider.EfetuarPagamentoProviderConverter;
import br.com.passella.operacaopagamento.provider.impl.CadastroOpenFeignClient.DeviceResponse;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class EfetuarPagamento {
    
    private final CadastroProvider cadastroProvider;
    private final EfetuarPagamentoProvider efetuarPagamentoProvider;
    private final EfetuarPagamentoProviderConverter efetuarPagamentoProviderConverter;

    public EfetuarPagamento(final CadastroProvider cadastroProvider, final EfetuarPagamentoProvider efetuarPagamentoProvider, final EfetuarPagamentoProviderConverter efetuarPagamentoProviderConverter) {
        this.cadastroProvider = cadastroProvider;
        this.efetuarPagamentoProvider = efetuarPagamentoProvider;
        this.efetuarPagamentoProviderConverter = efetuarPagamentoProviderConverter;
    }

    public Mono<EfetuarPagamentoOutput> execute(final Mono<EfetuarPagamentoInput> pagamentoInput){
        return pagamentoInput
                .flatMap(input -> buildDadosPagamentos(cadastroProvider.getDevice(input.idDevice()), input))
                .flatMap(efetuarPagamentoProvider::send)
                .map(efetuarPagamentoProviderConverter::toOutput);

    }

    private Mono<DadosPagamento> buildDadosPagamentos(final DeviceResponse device, final EfetuarPagamentoInput pagamento) {
        return Mono.just(new DadosPagamento(device, pagamento));
    }

    public record EfetuarPagamentoOutput(DadosPagamento device, String idPagamento, StatusPagamento status) {
    }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record EfetuarPagamentoInput(String idDevice, Double valor) {
    }


}
