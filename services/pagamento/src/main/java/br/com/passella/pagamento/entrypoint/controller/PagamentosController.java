package br.com.passella.pagamento.entrypoint.controller;

import br.com.passella.pagamento.domain.model.Pagamento;
import br.com.passella.pagamento.entrypoint.controller.converter.PagamentosControllerConverter;
import br.com.passella.pagamento.usecase.ObterPagamentos;
import br.com.passella.pagamento.usecase.ObterPagamentos.ObterPagamentosInput;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.text.MessageFormat;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/v1/pagamentos")
public class PagamentosController {

    protected final Logger logger = Logger.getLogger(getClass().getName());
    private final ObterPagamentos obterPagamentos;
    private final PagamentosControllerConverter pagamentosControllerConverter;

    public PagamentosController(final ObterPagamentos obterPagamentos, final PagamentosControllerConverter pagamentosControllerConverter) {
        this.obterPagamentos = obterPagamentos;
        this.pagamentosControllerConverter = pagamentosControllerConverter;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Flux<Pagamento> getPagamentos(@PathVariable String id) {
        logger.info(MessageFormat.format("Obtendo pagamentos do estabelecimento: {0}", id));
        return obterPagamentos
                .execute(new ObterPagamentosInput(id))
                .flatMap(obterPagamentosOutput -> Mono.just(pagamentosControllerConverter.toOutput(obterPagamentosOutput)));
    }
}
