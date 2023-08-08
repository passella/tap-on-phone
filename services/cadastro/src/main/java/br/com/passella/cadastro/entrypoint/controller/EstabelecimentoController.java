package br.com.passella.cadastro.entrypoint.controller;

import br.com.passella.cadastro.config.RateLimiterProperties;
import br.com.passella.cadastro.domain.model.EstabelecimentoRequest;
import br.com.passella.cadastro.domain.model.EstabelecimentoResponse;
import br.com.passella.cadastro.entrypoint.controller.converter.EstabelecimentoControllerConverter;
import br.com.passella.cadastro.usecase.CadastrarEstabelecimento;
import br.com.passella.cadastro.usecase.converter.CadastrarEstabelecimentoConverter;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.text.MessageFormat;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/v1/estabelecimentos")
public class EstabelecimentoController extends AbstractController {

    private final Logger logger = Logger.getLogger(EstabelecimentoController.class.getName());
    private final CadastrarEstabelecimento cadastrarEstabelecimento;
    private final CadastrarEstabelecimentoConverter cadastrarEstabelecimentoConverter;
    private final EstabelecimentoControllerConverter estabelecimentoControllerConverter;


    public EstabelecimentoController(final CadastrarEstabelecimento cadastrarEstabelecimento, final CadastrarEstabelecimentoConverter cadastrarEstabelecimentoConverter, final EstabelecimentoControllerConverter estabelecimentoControllerConverter, final RateLimiterProperties rateLimiterProperties) {
        super(rateLimiterProperties);
        this.cadastrarEstabelecimento = cadastrarEstabelecimento;
        this.cadastrarEstabelecimentoConverter = cadastrarEstabelecimentoConverter;
        this.estabelecimentoControllerConverter = estabelecimentoControllerConverter;
    }


    @PostMapping
    public Mono<ResponseEntity<EstabelecimentoResponse>> createEstabelecimento(@Valid @RequestBody final Mono<@Valid EstabelecimentoRequest> estabelecimentoRequest) {
        return monoLimiter(estabelecimentoRequest
                .doOnNext(estabelecimento -> logger.info(MessageFormat.format("Criando estabelecimento: {0}", estabelecimento.getNome())))
                .map(estabelecimento -> Mono.just(cadastrarEstabelecimentoConverter.toInput(estabelecimento)))
                .flatMap(input -> flatMapRetry(cadastrarEstabelecimento.execute(input)))
                .map(estabelecimentoControllerConverter::toEstabelecimentoResponse)
                .doOnNext(estabelecimento -> logger.info(MessageFormat.format("Estabelecimento {0}:{1} criado com sucesso", estabelecimento.id(), estabelecimento.nome()))))
                .map(estabelecimentoResponse -> ResponseEntity.status(HttpStatus.CREATED).body(estabelecimentoResponse));
    }

}
