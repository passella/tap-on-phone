package br.com.passella.motorpagamento.usecase;

import br.com.passella.motorpagamento.domain.entity.PagamentoEntity;
import br.com.passella.motorpagamento.domain.model.HistoricoPagamento;
import br.com.passella.motorpagamento.domain.model.StatusPagamento;
import br.com.passella.motorpagamento.error.exception.PagamentosRepositoryNotFountException;
import br.com.passella.motorpagamento.repository.PagamentosRepository;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.logging.Logger;

@Component
public class AtualizarPagamento {
    protected final Logger logger = Logger.getLogger(getClass().getName());
    private final PagamentosRepository pagamentosRepository;

    public AtualizarPagamento(final PagamentosRepository pagamentosRepository) {
        this.pagamentosRepository = pagamentosRepository;
    }

    public AtualizarPagamentoOutput execute(final AtualizarPagamentoInput input) throws PagamentosRepositoryNotFountException {
        final var pagamentoEntity = pagamentosRepository
                .findById(input.idPagamento())
                .orElseThrow(() -> new PagamentosRepositoryNotFountException("Pagamento não localizado: " + input.idPagamento()));

        final var pagamentoAlterado = switch (input.statusPagamento()) {
            case DESFEITO -> desfazerPagamento(input, pagamentoEntity);
            case CANCELADO -> cancelarPagamento(input, pagamentoEntity);
            case CONFIRMADO -> confirmarPagamento(input, pagamentoEntity);
            default -> false;
        };

        return new AtualizarPagamentoOutput(pagamentoAlterado);
    }

    private boolean confirmarPagamento(final AtualizarPagamentoInput input, final PagamentoEntity pagamentoEntity) {
        if (pagamentoEntity.getStatus() == StatusPagamento.PENDENTE) {
            atualizarPagamento(input, pagamentoEntity);
            return true;
        }
        return false;
    }

    private boolean cancelarPagamento(final AtualizarPagamentoInput input, final PagamentoEntity pagamentoEntity) {
        if (Set.of(StatusPagamento.PENDENTE, StatusPagamento.CONFIRMADO).contains(pagamentoEntity.getStatus())) {
            atualizarPagamento(input, pagamentoEntity);
            return true;
        }
        return false;
    }

    private boolean desfazerPagamento(final AtualizarPagamentoInput input, final PagamentoEntity pagamentoEntity) {
        if (pagamentoEntity.getStatus() == StatusPagamento.PENDENTE) {
            atualizarPagamento(input, pagamentoEntity);
            return true;
        }
        return false;
    }

    private void atualizarPagamento(AtualizarPagamentoInput input, PagamentoEntity pagamentoEntity) {
        pagamentoEntity.addHistorico(new HistoricoPagamento(pagamentoEntity.getStatus(), input.statusPagamento(), LocalDateTime.now()));
        pagamentoEntity.setStatus(input.statusPagamento());
        logger.info(MessageFormat.format("Pagamento atualizado: {0}", pagamentoEntity));
        pagamentosRepository.save(pagamentoEntity);
    }

    public record AtualizarPagamentoInput(String id, String idPagamento, StatusPagamento statusPagamento) {
    }

    public record AtualizarPagamentoOutput(boolean pagamentoAlterado) {
    }
}
