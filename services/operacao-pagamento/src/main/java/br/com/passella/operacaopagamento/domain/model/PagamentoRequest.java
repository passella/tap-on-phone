package br.com.passella.operacaopagamento.domain.model;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Schema(description = "Efetuar um pagamento")
public class PagamentoRequest {
    @NotNull(message = "ID Device é obrigatório")
    @NotEmpty(message = "ID Device não pode estar em branco")
    @Schema(description = "ID do device que realizou o pagamento")
    private String idDevice;

    @NotNull(message = "Valor deve ser preenchido")
    @Positive(message = "Valor deve ser maior que zero")
    @Schema(description = "Valor do pagamento")
    private Double valor;

    public String getIdDevice() {
        return idDevice;
    }

    public Double getValor() {
        return valor;
    }
}
