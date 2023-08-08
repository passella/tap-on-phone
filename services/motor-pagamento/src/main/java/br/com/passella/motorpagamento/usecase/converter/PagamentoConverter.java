package br.com.passella.motorpagamento.usecase.converter;

import br.com.passella.motorpagamento.domain.entity.PagamentoEntity;
import br.com.passella.motorpagamento.usecase.CriarPagamento.CriarPagamentoInput;
import br.com.passella.motorpagamento.usecase.CriarPagamento.CriarPagamentoOutput;
import org.springframework.stereotype.Component;

@Component
public class PagamentoConverter {
    public PagamentoEntity toEntity(final CriarPagamentoInput input) {
        return new PagamentoEntity(input.idPagamento(), input.device(), input.status());
    }

    public CriarPagamentoOutput toOutput(final PagamentoEntity entity) {
        return new CriarPagamentoOutput(entity.getIdPagamento(), entity.getDevice(), entity.getStatus());
    }
}
