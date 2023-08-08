package br.com.passella.operacaopagamento.entrypoint.controller;

import br.com.passella.operacaopagamento.domain.model.PagamentoResponse;
import br.com.passella.operacaopagamento.domain.model.PatchResponse;
import br.com.passella.operacaopagamento.usecase.AtualizarStatus.AtualizarStatusOutput;
import br.com.passella.operacaopagamento.usecase.EfetuarPagamento.EfetuarPagamentoOutput;
import org.springframework.stereotype.Component;

@Component
public class PagamentoControllerConverter {
    public PagamentoResponse toResponse(final EfetuarPagamentoOutput output) {
        return new PagamentoResponse(output.device(), output.idPagamento(), output.status());
    }

    public PatchResponse toResponse(AtualizarStatusOutput output) {
        return new PatchResponse(output.id(), output.idPagamento(), output.statusPagamento());
    }
}
