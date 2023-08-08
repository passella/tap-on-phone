package br.com.passella.cadastro.usecase;

import br.com.passella.cadastro.error.exception.RepositoryException;
import br.com.passella.cadastro.repository.EstabelecimentoRepository;
import br.com.passella.cadastro.service.kafka.BigDataService;
import br.com.passella.cadastro.usecase.converter.CadastrarEstabelecimentoConverter;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class CadastrarEstabelecimento {

    private final EstabelecimentoRepository estabelecimentoRepository;
    private final CadastrarEstabelecimentoConverter cadastrarEstabelecimentoConverter;
    private final BigDataService bigDataService;

    public CadastrarEstabelecimento(final EstabelecimentoRepository estabelecimentoRepository, final CadastrarEstabelecimentoConverter cadastrarEstabelecimentoConverter, final BigDataService bigDataService) {
        this.estabelecimentoRepository = estabelecimentoRepository;
        this.cadastrarEstabelecimentoConverter = cadastrarEstabelecimentoConverter;
        this.bigDataService = bigDataService;
    }

    public Mono<CadastrarEstabelecimentoOutput> execute(final Mono<CadastrarEstabelecimentoInput> input) {
        return input
                .flatMap(cadastrarEstabelecimentoInput -> {
                    try {
                        final var estabelecimentoEntity = estabelecimentoRepository.save(cadastrarEstabelecimentoConverter.toEntity(cadastrarEstabelecimentoInput));
                        bigDataService.send(estabelecimentoEntity);
                        return Mono.just(cadastrarEstabelecimentoConverter.toOutput(estabelecimentoEntity));
                    } catch (final RepositoryException e) {
                        return Mono.error(e);
                    }
                });
    }

    public record CadastrarEstabelecimentoOutput(String id, String nome) {
    }

    public record CadastrarEstabelecimentoInput(String nome) {
    }
}
