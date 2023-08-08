package br.com.passella.pagamento.domain.entity;


import br.com.passella.pagamento.domain.model.DadosPagamento;
import br.com.passella.pagamento.domain.model.HistoricoPagamento;
import br.com.passella.pagamento.domain.model.StatusPagamento;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Document("pagamentos")
public class PagamentoEntity {

    @Id
    private String idPagamento;

    @Field("device")
    private DadosPagamento dadosPagamento;

    private StatusPagamento status;
    private List<HistoricoPagamento> historico;

    public PagamentoEntity() {
    }

    public PagamentoEntity(final String idPagamento, final DadosPagamento dadosPagamento, final StatusPagamento status) {
        this.idPagamento = idPagamento;
        this.dadosPagamento = dadosPagamento;
        this.status = status;
    }


    public String getIdPagamento() {
        return idPagamento;
    }

    public DadosPagamento getDadosPagamento() {
        return dadosPagamento;
    }

    public StatusPagamento getStatus() {
        return status;
    }

    public void setStatus(final StatusPagamento status) {
        this.status = status;
    }

    public List<HistoricoPagamento> getHistorico() {
        return historico;
    }

    public void addHistorico(final HistoricoPagamento historico) {
        final var pagamentos = Optional.ofNullable(this.historico).orElseGet(List::of);
        this.historico = Stream
                .of(pagamentos, List.of(historico))
                .flatMap(Collection::stream)
                .toList();
    }


}
