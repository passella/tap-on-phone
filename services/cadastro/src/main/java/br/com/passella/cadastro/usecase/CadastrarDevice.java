package br.com.passella.cadastro.usecase;

import br.com.passella.cadastro.dao.DeviceDao;
import br.com.passella.cadastro.error.exception.RepositoryException;
import br.com.passella.cadastro.service.kafka.BigDataService;
import br.com.passella.cadastro.usecase.CadastrarEstabelecimento.CadastrarEstabelecimentoOutput;
import br.com.passella.cadastro.usecase.converter.CadastrarDeviceConverter;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class CadastrarDevice {
    private final DeviceDao deviceDao;
    private final CadastrarDeviceConverter cadastrarDeviceConverter;
    private final BigDataService bigDataService;

    public CadastrarDevice(final DeviceDao deviceDao, final CadastrarDeviceConverter cadastrarDeviceConverter, final BigDataService bigDataService) {
        this.deviceDao = deviceDao;
        this.cadastrarDeviceConverter = cadastrarDeviceConverter;
        this.bigDataService = bigDataService;
    }


    public Mono<CadastrarDeviceOutput> execute(final Mono<CadastrarDeviceInput> input) {
        return input
                .flatMap(deviceInput -> {
                    try {
                        final var deviceDaoInput = cadastrarDeviceConverter.toEntity(deviceInput);
                        final var saved = deviceDao.save(deviceDaoInput);
                        bigDataService.send(saved);
                        return Mono.just(cadastrarDeviceConverter.toOutput(saved));
                    } catch (RepositoryException e) {
                        return Mono.error(e);
                    }
                });
    }

    public record CadastrarDeviceOutput(String id, String nome, List<CadastrarEstabelecimentoOutput> estabelecimentos) {
    }

    public record CadastrarDeviceInput(String nome, List<String> estabelecimentos) {
    }
}
