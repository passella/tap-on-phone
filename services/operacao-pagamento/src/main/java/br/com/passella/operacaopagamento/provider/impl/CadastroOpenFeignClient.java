package br.com.passella.operacaopagamento.provider.impl;

import br.com.passella.operacaopagamento.config.OpenFeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "dadosPagamento-service", url = "${spring.cloud.openfeign.client.config.dadosPagamento-service.url}", configuration = OpenFeignConfig.class)
public interface CadastroOpenFeignClient {
    @GetMapping("/{id}")
    ResponseEntity<DeviceResponse> getDevice(@PathVariable String id);

    record DeviceResponse(String id, String nome, List<Estabelecimento> estabelecimentos) {
    }

    record Estabelecimento(String id, String nome) {
    }

}
