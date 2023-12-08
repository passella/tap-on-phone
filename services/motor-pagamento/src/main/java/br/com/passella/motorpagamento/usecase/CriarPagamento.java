package br.com.passella.motorpagamento.usecase;

import br.com.passella.motorpagamento.domain.model.DadosPagamento;
import br.com.passella.motorpagamento.domain.model.StatusPagamento;
import br.com.passella.motorpagamento.repository.PagamentosRepository;
import br.com.passella.motorpagamento.usecase.converter.PagamentoConverter;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.springframework.stereotype.Component;

@Component
public class CriarPagamento {

    private final PagamentosRepository pagamentosRepository;
    private final PagamentoConverter pagamentoConverter;

    public CriarPagamento(final PagamentosRepository pagamentosRepository, final PagamentoConverter pagamentoConverter) {
        this.pagamentosRepository = pagamentosRepository;
        this.pagamentoConverter = pagamentoConverter;
    }

    public CriarPagamentoOutput execute(final CriarPagamentoInput input) {
        final var pagamentoEntity = pagamentoConverter.toEntity(input);
        final var saved = pagamentosRepository.save(pagamentoEntity);
        return pagamentoConverter.toOutput(saved);
    }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record CriarPagamentoInput(DadosPagamento device, String idPagamento, StatusPagamento status) {
    }

    public record CriarPagamentoOutput(String idPagamento, DadosPagamento device, StatusPagamento status) {
    }
}
