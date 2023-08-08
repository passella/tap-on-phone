package br.com.passella.operacaopagamento.provider.impl;

import br.com.passella.operacaopagamento.provider.CadastroProvider;
import br.com.passella.operacaopagamento.provider.impl.CadastroOpenFeignClient.DeviceResponse;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
public class HttpRestCadastroProvider implements CadastroProvider {

    private final CadastroOpenFeignClient feignClient;

    public HttpRestCadastroProvider(final CadastroOpenFeignClient feignClient) {
        this.feignClient = feignClient;
    }

    @Override
    @Cacheable(value = "getDevice")
    public DeviceResponse getDevice(final String id) {
        return feignClient.getDevice(id).getBody();
    }
}
