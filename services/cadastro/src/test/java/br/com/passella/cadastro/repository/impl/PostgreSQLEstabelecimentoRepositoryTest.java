package br.com.passella.cadastro.repository.impl;

import br.com.passella.cadastro.domain.entity.EstabelecimentoEntity;
import br.com.passella.cadastro.error.exception.PostgreSQLException;
import br.com.passella.cadastro.error.exception.RepositoryException;
import br.com.passella.cadastro.utils.ContainersForTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@SpringBootTest
class PostgreSQLEstabelecimentoRepositoryTest {

    @Autowired
    private PostgreSQLEstabelecimentoRepository cadastroRepository;
    @MockBean
    private JdbcTemplate jdbcTemplate;
    private final EstabelecimentoEntity entity = buildEstabelecimentoEntity();

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

    private EstabelecimentoEntity buildEstabelecimentoEntity() {
        final var estabelecimentoEntity = new EstabelecimentoEntity();
        estabelecimentoEntity.setNome("nome");
        return estabelecimentoEntity;
    }

    @Test
    @DisplayName("Deve salvar com sucesso")
    void saveSucesso() throws RepositoryException {
        doReturn(1)
                .when(jdbcTemplate)
                .update(anyString(), anyString(), anyString());
        final var saved = cadastroRepository.save(entity);
        assertThat(saved)
                .isNotNull();
        assertThat(saved.getId())
                .isNotNull();
        assertThat(saved.getNome())
                .isEqualTo(entity.getNome());
    }

    @Test
    @DisplayName("Deve apresentar erro quando não inserir registros")
    void saveFalhaInsert() {
        doReturn(0)
                .when(jdbcTemplate)
                .update(anyString(), anyString(), anyString());
        assertThrows(PostgreSQLException.class, () -> cadastroRepository.save(entity));
    }

    @Test
    @DisplayName("Deve apresentar erro original")
    void saveFalhaOriginalInsert() {
        doThrow(new RuntimeException("Erro qualquer"))
                .when(jdbcTemplate)
                .update(anyString(), anyString(), anyString());
        final var runtimeException = assertThrows(RuntimeException.class, () -> cadastroRepository.save(entity));
        assertThat(runtimeException.getMessage()).isEqualTo("Erro qualquer");
    }

}
