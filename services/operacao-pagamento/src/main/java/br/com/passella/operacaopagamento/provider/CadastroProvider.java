package br.com.passella.operacaopagamento.provider;

import br.com.passella.operacaopagamento.provider.impl.CadastroOpenFeignClient.DeviceResponse;

public interface CadastroProvider {
    DeviceResponse getDevice(String idDevice);


}
