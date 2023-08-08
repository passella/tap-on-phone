package br.com.passella.operacaopagamento.provider;


import br.com.passella.operacaopagamento.provider.EfetuarPagamentoProvider.PagamentoEfetuado;
import br.com.passella.operacaopagamento.usecase.EfetuarPagamento.EfetuarPagamentoOutput;
import org.springframework.stereotype.Component;

@Component
public class EfetuarPagamentoProviderConverter {

    public EfetuarPagamentoOutput toOutput(PagamentoEfetuado pagamentoEfetuado) {
        return new EfetuarPagamentoOutput(pagamentoEfetuado.device(), pagamentoEfetuado.idPagamento(), pagamentoEfetuado.status());
    }
}
