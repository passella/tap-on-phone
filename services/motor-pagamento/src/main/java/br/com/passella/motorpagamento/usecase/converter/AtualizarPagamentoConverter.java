package br.com.passella.motorpagamento.usecase.converter;

import br.com.passella.motorpagamento.domain.model.AtualizarStatusInput;
import br.com.passella.motorpagamento.usecase.AtualizarPagamento.AtualizarPagamentoInput;
import org.springframework.stereotype.Component;

@Component
public class AtualizarPagamentoConverter {
    public AtualizarPagamentoInput toInput(AtualizarStatusInput atualizarStatusInput) {
        return new AtualizarPagamentoInput(atualizarStatusInput.id(), atualizarStatusInput.idPagamento(), atualizarStatusInput.statusPagamento());
    }
}
