package br.com.passella.cadastro.usecase;

import br.com.passella.cadastro.dao.DeviceDao;
import br.com.passella.cadastro.domain.entity.DeviceEntity;
import br.com.passella.cadastro.error.exception.RepositoryException;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class ObterDevice {

    private final DeviceDao deviceDao;

    public ObterDevice(final DeviceDao deviceDao) {
        this.deviceDao = deviceDao;
    }


    public Mono<DeviceEntity> execute(String id){
        try {
            return Mono.just(deviceDao.get(id));
        } catch (RepositoryException e) {
            return Mono.error(e);
        }
    }
}
