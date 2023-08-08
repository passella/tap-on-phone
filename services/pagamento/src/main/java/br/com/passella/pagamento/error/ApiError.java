package br.com.passella.pagamento.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiError(HttpStatus status, String message, List<String> errors, ApiErrorRequest request) {
}
