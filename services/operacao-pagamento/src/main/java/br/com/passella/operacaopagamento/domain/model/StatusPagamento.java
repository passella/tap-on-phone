package br.com.passella.operacaopagamento.domain.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Status do pagamento")
public enum StatusPagamento {
    @Schema(description = "Pagamento pendente, o mesmo foi realizado mas ainda não foi processado")
    PENDENTE("pendente"),
    @Schema(description = "Pagamento cancelado, o mesmo foi confirmado e depois cancelado")
    CANCELADO("cancelado"),
    @Schema(description = "Pagamento confirmado, o mesmo foi processado com sucesso")
    CONFIRMADO("confirmado"),
    @Schema(description = "Pagamento desfeito antes da confirmação")
    DESFEITO("desfeito");

    StatusPagamento(final String value) {

    }
}
