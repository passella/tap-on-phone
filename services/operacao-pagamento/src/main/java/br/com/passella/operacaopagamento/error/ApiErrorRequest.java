package br.com.passella.operacaopagamento.error;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiErrorRequest(String id, String path, String method, List<Map.Entry<String, String>> headers) {
}
