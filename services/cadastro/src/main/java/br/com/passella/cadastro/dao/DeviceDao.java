package br.com.passella.cadastro.dao;

import br.com.passella.cadastro.domain.entity.DeviceEntity;
import br.com.passella.cadastro.error.exception.RepositoryException;

import java.util.List;

public interface DeviceDao {
    DeviceEntity save(DeviceDaoInput deviceDaoInput) throws RepositoryException;

    DeviceEntity get(String id) throws RepositoryException;

    class DeviceDaoInput {

        private String nome;

        private List<String> estabelecimentos;



        public String getNome() {
            return nome;
        }

        public List<String> getEstabelecimentos() {
            return estabelecimentos;
        }



        public void setNome(final String nome) {
            this.nome = nome;
        }

        public void setEstabelecimentos(final List<String> estabelecimentos) {
            this.estabelecimentos = estabelecimentos;
        }
    }
}
