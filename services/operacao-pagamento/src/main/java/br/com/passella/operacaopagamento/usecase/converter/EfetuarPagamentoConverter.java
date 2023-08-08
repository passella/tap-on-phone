package br.com.passella.operacaopagamento.usecase.converter;

import br.com.passella.operacaopagamento.domain.model.PagamentoRequest;
import br.com.passella.operacaopagamento.usecase.EfetuarPagamento.EfetuarPagamentoInput;
import org.springframework.stereotype.Component;

@Component
public class EfetuarPagamentoConverter {
    public EfetuarPagamentoInput toInput(PagamentoRequest request) {
        return new EfetuarPagamentoInput(request.getIdDevice(), request.getValor());
    }
}
