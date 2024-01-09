package br.com.passella.cadastro.config;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.annotation.PostConstruct;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@Profile("!no_flyway")
@Configuration
public class FlywayConfig {

    private final FlywayProperties flywayProperties;

    private final DataSource userDataSource;

    private final String userUser;

    public FlywayConfig(final FlywayProperties flywayProperties, final DataSource dataSource, @Value("${spring.datasource.username}") final String userUsername) {
        this.flywayProperties = flywayProperties;
        this.userDataSource = dataSource;
        this.userUser = userUsername;
    }


    @PostConstruct
    public void init() {
        Flyway
                .configure()
                .dataSource(initDataSource())
                .locations("db/migration/init")
                .placeholders(flywayProperties.getPlaceholders())
                .load()
                .migrate();

        Flyway
                .configure()
                .dataSource(userDataSource)
                .locations("db/migration/user")
                .placeholders(flywayProperties.getPlaceholders())
                .defaultSchema(userUser)
                .load()
                .migrate();
    }

    private DataSource initDataSource() {
        final var hikariDataSource = new HikariDataSource();
        hikariDataSource.setJdbcUrl(flywayProperties.getUrl());
        hikariDataSource.setUsername(flywayProperties.getUser());
        hikariDataSource.setPassword(flywayProperties.getPassword());
        return hikariDataSource;
    }

}
