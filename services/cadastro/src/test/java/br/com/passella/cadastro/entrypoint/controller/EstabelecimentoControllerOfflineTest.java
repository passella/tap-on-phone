package br.com.passella.cadastro.entrypoint.controller;

import br.com.passella.cadastro.domain.model.EstabelecimentoRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("no_flyway")
@SpringBootTest
@AutoConfigureWebTestClient(timeout = "10000")
@ImportTestcontainers
class EstabelecimentoControllerOfflineTest {


    @Autowired
    private WebTestClient webTestClient;


    @Test
    @DisplayName("Deve falhar, pois não conecta na base")
    void createEstabelecimentoCreated() {
        final var estabelecimentoRequest = new EstabelecimentoRequest("estabelecimento");
        webTestClient
                .post()
                .uri("/api/v1/estabelecimentos")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(estabelecimentoRequest))
                .exchange()
                .expectStatus()
                .is5xxServerError()
                .expectBody()
                .consumeWith(entityExchangeResult -> assertThat(entityExchangeResult.getResponseBody())
                        .asString()
                        .contains("Retries exhausted", "Failed to obtain JDBC Connection"));
    }


}
