package br.com.passella.cadastro.entrypoint.health;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;


@Component("cadastro")
public class CadastroHealthIndicator implements HealthIndicator {


    private final String podName;
    private final String version;
    private final String porNamespace;

    public CadastroHealthIndicator(final Environment environment, @Value("${application.version}") final String version) {
        podName = environment.getProperty("POD_NAME");
        porNamespace = environment.getProperty("POR_NAMESPACE");
        this.version = version;
    }

    @Override
    public Health health() {
        return Health.up().withDetail("status", MessageFormat.format("({0})({1})({2})", porNamespace, podName, version)).build();
    }
}
