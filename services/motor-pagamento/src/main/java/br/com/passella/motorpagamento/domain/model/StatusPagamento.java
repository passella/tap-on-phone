package br.com.passella.motorpagamento.domain.model;




public enum StatusPagamento {

    PENDENTE("pendente"),
    CANCELADO("cancelado"),
    CONFIRMADO("confirmado"),
    DESFEITO("desfeito");

    StatusPagamento(final String value) {

    }
}
