package br.com.passella.cadastro.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@ConfigurationProperties(prefix = "spring.flyway")
@Configuration
public class FlywayProperties {
    private Map<String, String> placeholders;
    private String url;
    private String user;
    private String password;


    public Map<String, String> getPlaceholders() {
        return placeholders;
    }

    public void setPlaceholders(final Map<String, String> placeholders) {
        this.placeholders = placeholders;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    public String getUser() {
        return user;
    }

    public void setUser(final String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }
}
