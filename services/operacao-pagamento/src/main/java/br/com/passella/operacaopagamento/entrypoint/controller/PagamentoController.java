package br.com.passella.operacaopagamento.entrypoint.controller;

import br.com.passella.operacaopagamento.config.RateLimiterProperties;
import br.com.passella.operacaopagamento.domain.model.PagamentoRequest;
import br.com.passella.operacaopagamento.domain.model.PagamentoResponse;
import br.com.passella.operacaopagamento.domain.model.PatchRequest;
import br.com.passella.operacaopagamento.domain.model.PatchResponse;
import br.com.passella.operacaopagamento.usecase.AtualizarStatus;
import br.com.passella.operacaopagamento.usecase.EfetuarPagamento;
import br.com.passella.operacaopagamento.usecase.converter.AtualizarStatusConverter;
import br.com.passella.operacaopagamento.usecase.converter.EfetuarPagamentoConverter;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.text.MessageFormat;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/v1/pagamentos")
public class PagamentoController extends AbstractController {
    private final Logger logger = Logger.getLogger(getClass().getName());
    
    private final EfetuarPagamento efetuarPagamento;
    private final EfetuarPagamentoConverter efetuarPagamentoConverter;
    private final PagamentoControllerConverter pagamentoControllerConverter;
    private final AtualizarStatus atualizarStatus;
    private final AtualizarStatusConverter atualizarStatusConverter;

    protected PagamentoController(RateLimiterProperties rateLimiterProperties, final EfetuarPagamento efetuarPagamento, final EfetuarPagamentoConverter efetuarPagamentoConverter, final PagamentoControllerConverter pagamentoControllerConverter, final AtualizarStatus atualizarStatus, final AtualizarStatusConverter atualizarStatusConverter) {
        super(rateLimiterProperties);
        this.efetuarPagamento = efetuarPagamento;
        this.efetuarPagamentoConverter = efetuarPagamentoConverter;
        this.pagamentoControllerConverter = pagamentoControllerConverter;
        this.atualizarStatus = atualizarStatus;
        this.atualizarStatusConverter = atualizarStatusConverter;
    }

    @PostMapping
    public Mono<ResponseEntity<PagamentoResponse>> efetuarPagamento(@Valid @RequestBody Mono<@Valid PagamentoRequest> pagamentoRequest) {
        return pagamentoRequest
                .doOnNext(request -> logger.info("Recebendo pagamento: " + request.getIdDevice()))
                .map(request -> Mono.just(efetuarPagamentoConverter.toInput(request)))
                .flatMap(input -> flatMapRetry(efetuarPagamento.execute(input)))
                .map(pagamentoControllerConverter::toResponse)
                .doOnNext(response -> logger.info(MessageFormat.format("Pagamento realizado: Device: {0}, ID Pagamento:{1}", response.dadosPagamento().device().id(), response.idPagamento())))
                .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response));
    }

    @PatchMapping
    public Mono<ResponseEntity<PatchResponse>> atualizarStatus(@Valid @RequestBody Mono<@Valid PatchRequest> pagamentoRequest){
        return pagamentoRequest
                .doOnNext(request -> logger.info("Recebendo atualização de pagamento: {" + request + "}"))
                .map(atualizarStatusConverter::toInput)
                .flatMap(atualizarStatus::execute)
                .map(pagamentoControllerConverter::toResponse)
                .doOnNext(response -> logger.info(MessageFormat.format("Solicitação de atualização efetuada: {0}", response)))
                .map(ResponseEntity::ok);
                
    }


}
