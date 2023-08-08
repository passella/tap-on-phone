package br.com.passella.operacaopagamento.domain.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record PatchRequest(
        @NotNull(message = "ID do pagamento é obrigatório")
        @NotEmpty(message = "ID do pagamento não pode estar em branco")
        String idPagamento,

        @NotNull(message = "Status do pagamento é obrigatório")
        StatusPagamento status
) {
}
