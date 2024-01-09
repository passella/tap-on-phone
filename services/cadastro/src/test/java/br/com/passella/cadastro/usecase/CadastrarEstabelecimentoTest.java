package br.com.passella.cadastro.usecase;

import br.com.passella.cadastro.domain.entity.EstabelecimentoEntity;
import br.com.passella.cadastro.error.exception.RepositoryException;
import br.com.passella.cadastro.repository.EstabelecimentoRepository;
import br.com.passella.cadastro.usecase.CadastrarEstabelecimento.CadastrarEstabelecimentoInput;
import br.com.passella.cadastro.usecase.converter.CadastrarEstabelecimentoConverter;
import br.com.passella.cadastro.utils.ContainersForTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;


@SpringBootTest
class CadastrarEstabelecimentoTest {

    @Autowired
    private CadastrarEstabelecimento cadastrarEstabelecimento;

    @MockBean
    private EstabelecimentoRepository estabelecimentoRepository;

    @MockBean
    private CadastrarEstabelecimentoConverter cadastrarEstabelecimentoConverter;

    private final CadastrarEstabelecimentoInput estabelecimentoX = new CadastrarEstabelecimentoInput("Estabelecimento X");
    private final Mono<CadastrarEstabelecimentoInput> input = Mono.just(estabelecimentoX);
    private final EstabelecimentoEntity entity = buildEstabelecimentoEntity(estabelecimentoX);

    private static ContainersForTest containersForTest;

    @BeforeAll
    static void beforeAll() {
        containersForTest = new ContainersForTest();
        containersForTest.beforeAll();
    }

    @AfterAll
    static void afterAll() {
        containersForTest.afterAll();
    }

    @DynamicPropertySource
    static void configureProperties(final DynamicPropertyRegistry registry) {
        containersForTest.configureProperties(registry);
    }

    @Test
    @DisplayName("Deve executar com sucesso")
    void executeSucesso() throws RepositoryException {
        doReturn(entity).when(cadastrarEstabelecimentoConverter).toEntity(estabelecimentoX);
        doReturn(entity).when(estabelecimentoRepository).save(entity);
        cadastrarEstabelecimento
                .execute(input)
                .subscribe();
        verify(estabelecimentoRepository, times(1)).save(entity);
        verify(cadastrarEstabelecimentoConverter, times(1)).toEntity(estabelecimentoX);
        verify(cadastrarEstabelecimentoConverter, times(1)).toOutput(entity);
    }

    @Test
    @DisplayName("Deve retornar erro original quando houver exceção")
    void executeFalha() throws RepositoryException {
        doReturn(entity).when(cadastrarEstabelecimentoConverter).toEntity(estabelecimentoX);
        doThrow(new RuntimeException("Erro Qualquer")).when(estabelecimentoRepository).save(any());
        StepVerifier.create(cadastrarEstabelecimento
                        .execute(input))
                .expectError(RuntimeException.class)
                .verify();
    }


    @Test
    @DisplayName("Deve retornar erro RepositoryException quando houver exceção")
    void executeFalhaRepositoryException() throws RepositoryException {
        doReturn(entity).when(cadastrarEstabelecimentoConverter).toEntity(estabelecimentoX);
        doThrow(new RepositoryException("Erro Qualquer")).when(estabelecimentoRepository).save(any());
        StepVerifier.create(cadastrarEstabelecimento
                        .execute(input))
                .expectError(RepositoryException.class)
                .verify();
    }


    private static EstabelecimentoEntity buildEstabelecimentoEntity(final CadastrarEstabelecimentoInput estabelecimentoX) {
        final var entity = new EstabelecimentoEntity();
        entity.setId(UUID.randomUUID().toString());
        entity.setNome(estabelecimentoX.nome());
        return entity;
    }
}
