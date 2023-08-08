package br.com.passella.cadastro.entrypoint.controller;

import br.com.passella.cadastro.config.RateLimiterProperties;
import br.com.passella.cadastro.domain.model.DeviceRequest;
import br.com.passella.cadastro.domain.model.DeviceResponse;
import br.com.passella.cadastro.entrypoint.controller.converter.DeviceControllerConverter;
import br.com.passella.cadastro.usecase.CadastrarDevice;
import br.com.passella.cadastro.usecase.ObterDevice;
import br.com.passella.cadastro.usecase.converter.CadastrarDeviceConverter;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.text.MessageFormat;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/v1/devices")
public class DeviceController extends AbstractController {

    private final Logger logger = Logger.getLogger(DeviceController.class.getName());
    private final CadastrarDevice cadastrarDevice;
    private final CadastrarDeviceConverter cadastrarDeviceConverter;
    private final DeviceControllerConverter deviceControllerConverter;
    private final ObterDevice obterDevice;


    public DeviceController(final RateLimiterProperties rateLimiterProperties, final CadastrarDevice cadastrarDevice, final CadastrarDeviceConverter cadastrarDeviceConverter, final DeviceControllerConverter deviceControllerConverter, final ObterDevice obterDevice) {
        super(rateLimiterProperties);
        this.cadastrarDevice = cadastrarDevice;
        this.cadastrarDeviceConverter = cadastrarDeviceConverter;
        this.deviceControllerConverter = deviceControllerConverter;
        this.obterDevice = obterDevice;
    }


    @PostMapping
    public Mono<ResponseEntity<DeviceResponse>> createDevice(@Valid @RequestBody final Mono<@Valid DeviceRequest> deviceRequest) {
        return monoLimiter(deviceRequest
                .doOnNext(device -> logger.info(MessageFormat.format("Criando device: {0}", device.getNome())))
                .map(device -> Mono.just(cadastrarDeviceConverter.toInput(device)))
                .flatMap(input -> flatMapRetry(cadastrarDevice.execute(input)))
                .map(deviceControllerConverter::toResponse)
                .doOnNext(response -> logger.info(MessageFormat.format("Device {0}:{1} criado com sucesso", response.id(), response.nome()))))
                .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<DeviceResponse>> getDevice(@PathVariable String id){
        logger.info(MessageFormat.format("Obtendo device: '{'{0}'}'", id));
        return obterDevice
                .execute(id)
                .map(deviceControllerConverter::toResponse)
                .doOnNext(deviceResponse -> logger.info(MessageFormat.format("Device obtido com sucesso: {0}", deviceResponse)))
                .map(ResponseEntity::ok);
    }



}
