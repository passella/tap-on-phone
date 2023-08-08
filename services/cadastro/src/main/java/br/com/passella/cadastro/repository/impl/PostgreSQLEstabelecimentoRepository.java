package br.com.passella.cadastro.repository.impl;

import br.com.passella.cadastro.domain.entity.EstabelecimentoEntity;
import br.com.passella.cadastro.error.exception.PostgreSQLException;
import br.com.passella.cadastro.error.exception.RepositoryException;
import br.com.passella.cadastro.repository.EstabelecimentoRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.text.MessageFormat;
import java.util.UUID;
import java.util.logging.Logger;

@Repository
public class PostgreSQLEstabelecimentoRepository implements EstabelecimentoRepository {

    private final JdbcTemplate jdbcTemplate;
    private final Logger logger = Logger.getLogger(PostgreSQLEstabelecimentoRepository.class.getName());

    public PostgreSQLEstabelecimentoRepository(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }



    @Override
    public EstabelecimentoEntity save(final EstabelecimentoEntity entity) throws RepositoryException {
        logger.info(MessageFormat.format("Cadastrando estabelecimento: {0}", entity.getNome()));
        
        final var estabelecimentoEntity = new EstabelecimentoEntity();
        estabelecimentoEntity.setId(UUID.randomUUID().toString());
        estabelecimentoEntity.setNome(entity.getNome());

        final var update = jdbcTemplate
                .update("insert into estabelecimento (id, nome) values (?, ?)",
                        estabelecimentoEntity.getId(),
                        estabelecimentoEntity.getNome());
        
        if (update == 0) {
            throw new PostgreSQLException(MessageFormat.format("Não foi possivel cadastrar a entidade: {0}", entity));
        }

        return estabelecimentoEntity;
    }
}
