package br.com.passella.operacaopagamento.domain.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Schema(description = "Dados da solicitação de pagamento")
public record PatchResponse(
        @Schema(description = "ID da solicitação de pagamento")
        String id,
        @Schema(description = "ID do pagamento realizado")
        String idPagamento,
        @Schema(description = "Status do pagamento")
        StatusPagamento statusPagamento
) {
}
