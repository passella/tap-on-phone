package br.com.passella.cadastro.usecase.converter;

import br.com.passella.cadastro.dao.DeviceDao.DeviceDaoInput;
import br.com.passella.cadastro.domain.entity.DeviceEntity;
import br.com.passella.cadastro.domain.model.DeviceRequest;
import br.com.passella.cadastro.usecase.CadastrarDevice.CadastrarDeviceInput;
import br.com.passella.cadastro.usecase.CadastrarDevice.CadastrarDeviceOutput;
import org.springframework.stereotype.Component;

@Component
public class CadastrarDeviceConverter {

    private final CadastrarEstabelecimentoConverter estabelecimentoConverter;

    public CadastrarDeviceConverter(final CadastrarEstabelecimentoConverter estabelecimentoConverter) {
        this.estabelecimentoConverter = estabelecimentoConverter;
    }

    public DeviceDaoInput toEntity(CadastrarDeviceInput deviceInput) {
        var deviceDaoInput = new DeviceDaoInput();
        deviceDaoInput.setEstabelecimentos(deviceInput.estabelecimentos());
        deviceDaoInput.setNome(deviceInput.nome());
        return deviceDaoInput;
    }

    public CadastrarDeviceOutput toOutput(DeviceEntity entity) {

        var estabelecimentos = entity
                .getEstabelecimentos()
                .stream()
                .map(estabelecimentoConverter::toOutput)
                .toList();

        return new CadastrarDeviceOutput(entity.getId(), entity.getNome(),estabelecimentos);
    }

    public CadastrarDeviceInput toInput(DeviceRequest device) {
        return new CadastrarDeviceInput(device.getNome(), device.getEstabelecimentos());
    }
}
