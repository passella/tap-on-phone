package br.com.passella.operacaopagamento.usecase.converter;

import br.com.passella.operacaopagamento.domain.model.PatchRequest;
import br.com.passella.operacaopagamento.usecase.AtualizarStatus.AtualizarStatusInput;
import org.springframework.stereotype.Component;

@Component
public class AtualizarStatusConverter {

    public AtualizarStatusInput toInput(PatchRequest request) {
        return new AtualizarStatusInput(request.idPagamento(), request.status());
    }
}
