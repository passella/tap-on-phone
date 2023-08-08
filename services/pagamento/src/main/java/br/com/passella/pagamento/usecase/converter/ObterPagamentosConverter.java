package br.com.passella.pagamento.usecase.converter;

import br.com.passella.pagamento.domain.entity.PagamentoEntity;
import br.com.passella.pagamento.usecase.ObterPagamentos.ObterPagamentosOutput;
import org.springframework.stereotype.Component;

@Component
public class ObterPagamentosConverter {
    public ObterPagamentosOutput toOutput(PagamentoEntity pagamentoEntity) {
        return new ObterPagamentosOutput(pagamentoEntity.getIdPagamento(), pagamentoEntity.getDadosPagamento(), pagamentoEntity.getStatus(), pagamentoEntity.getHistorico());
    }
}
