package br.com.passella.cadastro.entrypoint.controller.converter;

import br.com.passella.cadastro.domain.entity.DeviceEntity;
import br.com.passella.cadastro.domain.entity.converter.EstabelecimentoConverter;
import br.com.passella.cadastro.domain.model.DeviceResponse;
import br.com.passella.cadastro.usecase.CadastrarDevice.CadastrarDeviceOutput;
import org.springframework.stereotype.Component;

@Component
public class DeviceControllerConverter {

    private final EstabelecimentoConverter estabelecimentoConverter;

    public DeviceControllerConverter(final EstabelecimentoConverter estabelecimentoConverter) {
        this.estabelecimentoConverter = estabelecimentoConverter;
    }


    public DeviceResponse toResponse(final CadastrarDeviceOutput cadastrarDeviceOutput) {
        return new DeviceResponse(cadastrarDeviceOutput.id(), cadastrarDeviceOutput.nome(), cadastrarDeviceOutput.estabelecimentos());
    }

    public DeviceResponse toResponse(final DeviceEntity output) {
        final var estabelecimentos = output
                .getEstabelecimentos()
                .stream()
                .map(estabelecimentoConverter::toOutput)
                .toList();
        return new DeviceResponse(output.getId(), output.getNome(), estabelecimentos);
    }
}
