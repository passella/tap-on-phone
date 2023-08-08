package br.com.passella.pagamento.domain.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Dados do pagamento")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record Pagamento(
        @Schema(description = "ID do pagamento")
        String idPagamento,
        @Schema(description = "Dados do pagamento")
        DadosPagamento dadosPagamento,
        @Schema(description = "Status do pagamento")
        StatusPagamento statusPagamento,
        @Schema(description = "Histório de pagamentos")
        List<HistoricoPagamento> historico
) {

}
