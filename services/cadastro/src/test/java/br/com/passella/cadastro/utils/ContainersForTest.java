package br.com.passella.cadastro.utils;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.testcontainers.containers.PostgreSQLContainer;

@SuppressWarnings("java:S2187")
public class ContainersForTest {
    private PostgreSQLContainer<?> postgreSQLContainer;

    public void beforeAll() {
        postgreSQLContainer = new PostgreSQLContainer<>("postgres:16.1")
                .withDatabaseName("postgres")
                .withUsername("postgres")
                .withPassword("postgres");
        postgreSQLContainer.start();
    }

    public void afterAll() {
        postgreSQLContainer.stop();
        postgreSQLContainer.close();
    }


    public void configureProperties(final DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.flyway.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.flyway.user", postgreSQLContainer::getUsername);
        registry.add("spring.flyway.password", postgreSQLContainer::getPassword);
    }


}
