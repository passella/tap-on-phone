package br.com.passella.cadastro.dao.impl;

import br.com.passella.cadastro.dao.DeviceDao;
import br.com.passella.cadastro.domain.entity.DeviceEntity;
import br.com.passella.cadastro.domain.entity.EstabelecimentoEntity;
import br.com.passella.cadastro.error.exception.PostgreSQLException;
import br.com.passella.cadastro.error.exception.PostgreSQLNotFoundException;
import br.com.passella.cadastro.error.exception.RepositoryException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Stream;

@Repository
public class PostgreSQLDeviceDao implements DeviceDao {

    private final JdbcTemplate jdbcTemplate;

    private final Logger logger = Logger.getLogger(PostgreSQLDeviceDao.class.getName());

    public PostgreSQLDeviceDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;

    }

    @Override
    @Transactional
    public DeviceEntity save(final DeviceDaoInput deviceDaoInput) throws RepositoryException {
        logger.info(MessageFormat.format("Cadastrando device: {0}", deviceDaoInput.getNome()));

        final var estabelecimentosIds = new HashSet<>(deviceDaoInput
                .getEstabelecimentos());

        final List<EstabelecimentoEntity> estabelecimentos = new ArrayList<>();
        for (final var estabelecimentoId : estabelecimentosIds) {
            final var estabelecimentoEntities = jdbcTemplate.query("select * from estabelecimento where id = ?", (resultSet, rowNum) -> {
                final var estabelecimentoEntity = new EstabelecimentoEntity();
                estabelecimentoEntity.setId(resultSet.getString(1));
                estabelecimentoEntity.setNome(resultSet.getString(2));
                return estabelecimentoEntity;
            }, estabelecimentoId);

            if (estabelecimentoEntities.isEmpty()) {
                throw new PostgreSQLNotFoundException("Estabelecimento {" + estabelecimentoId + "} não existe");
            }

            estabelecimentos.addAll(estabelecimentoEntities);
        }

        final var deviceEntity = new DeviceEntity();
        deviceEntity.setId(UUID.randomUUID().toString());
        deviceEntity.setNome(deviceDaoInput.getNome());
        deviceEntity.setEstabelecimentos(estabelecimentos);

        final var insertDevice = jdbcTemplate
                .update("insert into device (id, nome) values (?, ?)",
                        deviceEntity.getId(),
                        deviceEntity.getNome());

        if (insertDevice == 0) {
            throw new PostgreSQLException(MessageFormat.format("Não foi possivel cadastrar a entidade: {0}", deviceEntity));
        }

        for (final var estabelecimento : estabelecimentos) {
            final var insertRelacao = jdbcTemplate
                    .update("insert into device_estabelecimento (id, id_device, id_estabelecimento) values (?, ?, ?)",
                            UUID.randomUUID().toString(),
                            deviceEntity.getId(),
                            estabelecimento.getId());

            if (insertRelacao == 0) {
                throw new PostgreSQLException(MessageFormat.format("Não foi possivel cadastrar a relação: {0}: {1}", deviceEntity, estabelecimento));
            }
        }


        return deviceEntity;
    }

    @Override
    public DeviceEntity get(final String id) throws RepositoryException {
        final var sql = "select " +
                "	d.id deviceId, " +
                "	d.nome deviceName, " +
                "	e.id estabelecimentoId, " +
                "	e.nome estabelecimentoNome " +
                "from " +
                "	device d " +
                "inner join device_estabelecimento de on " +
                "	d.id = de.id_device " +
                "inner join estabelecimento e on " +
                "	de.id_estabelecimento = e.id " +
                "where " +
                "	d.id = ? ";
        final var deviceEntities = jdbcTemplate
                .query(sql, (resultSet, rowNum) -> {
                    final var estabelecimentoEntity = new EstabelecimentoEntity();
                    estabelecimentoEntity.setId(resultSet.getString(3));
                    estabelecimentoEntity.setNome(resultSet.getString(4));

                    final var deviceEntity = new DeviceEntity();
                    deviceEntity.setId(resultSet.getString(1));
                    deviceEntity.setNome(resultSet.getString(2));
                    deviceEntity.setEstabelecimentos(List.of(estabelecimentoEntity));
                    return deviceEntity;
                }, id);

        if (deviceEntities.isEmpty()) {
            throw new PostgreSQLNotFoundException(MessageFormat.format("Device '{'{0}'}' não cadastrado", id));
        }

        return deviceEntities
                .stream()
                .reduce((deviceEntity, deviceEntity2) -> {
                    final var entity = new DeviceEntity();
                    entity.setId(deviceEntity.getId());
                    entity.setNome(deviceEntity.getNome());
                    entity.setEstabelecimentos(Stream
                            .of(deviceEntity.getEstabelecimentos(), deviceEntity2.getEstabelecimentos())
                            .flatMap(Collection::stream)
                            .toList());
                    return entity;
                })
                .orElseThrow(() -> new PostgreSQLException(MessageFormat.format("Device '{'{0}'}' não cadastrado", id)));
    }
}
