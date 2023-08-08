package br.com.passella.pagamento.entrypoint.controller.converter;

import br.com.passella.pagamento.domain.model.Pagamento;
import br.com.passella.pagamento.usecase.ObterPagamentos.ObterPagamentosOutput;
import org.springframework.stereotype.Component;

@Component
public class PagamentosControllerConverter {

    public Pagamento toOutput(ObterPagamentosOutput output) {
        return new Pagamento(output.idPagamento(), output.dadosPagamento(), output.statusPagamento(), output.historico());
    }
}
