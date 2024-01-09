package br.com.passella.cadastro.entrypoint.controller;

import br.com.passella.cadastro.domain.model.EstabelecimentoRequest;
import br.com.passella.cadastro.utils.ContainersForTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureWebTestClient(timeout = "10000")
@ImportTestcontainers
class EstabelecimentoControllerTest {


    private static ContainersForTest containersForTest;

    @Autowired
    private WebTestClient webTestClient;

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
    @DisplayName("Deve retornar BadRequest quando campo obrigatório não for informado")
    void createEstabelecimentoBadRequest() {
        final var estabelecimentoRequest = new EstabelecimentoRequest(null);
        webTestClient
                .post()
                .uri("/api/v1/estabelecimentos")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(estabelecimentoRequest))
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody()
                .consumeWith(entityExchangeResult -> assertThat(entityExchangeResult.getResponseBody())
                        .asString()
                        .contains("Invalid request content"));
    }

    @Test
    @DisplayName("Deve retornar BadRequest quando passar qualquer coisa no body")
    void createEstabelecimentoBadRequestQualquerCoisa() {
        webTestClient
                .post()
                .uri("/api/v1/estabelecimentos")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue("qualquer coisa"))
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody()
                .consumeWith(entityExchangeResult -> assertThat(entityExchangeResult.getResponseBody())
                        .asString()
                        .contains("Failed to read HTTP message", "JSON decoding error: Unrecognized token"));
    }

    @Test
    @DisplayName("Deve retornar BadRequest quando não passar body")
    void createEstabelecimentoBadRequestNoBody() {
        webTestClient
                .post()
                .uri("/api/v1/estabelecimentos")
                .contentType(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody()
                .consumeWith(entityExchangeResult -> assertThat(entityExchangeResult.getResponseBody())
                        .asString()
                        .contains("No request body"));
    }

    @Test
    @DisplayName("Deve criar estabelecimento corretamente")
    void createEstabelecimentoCreated() {
        final var estabelecimentoRequest = new EstabelecimentoRequest("estabelecimento");
        webTestClient
                .post()
                .uri("/api/v1/estabelecimentos")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(estabelecimentoRequest))
                .exchange()
                .expectStatus()
                .isCreated();
    }

    @Test
    @DisplayName("Deve retornar NotFound quando passar uma rota não mapeada")
    void createEstabelecimentoNotFoundRotaNaoMapeada() {
        final var estabelecimentoRequest = new EstabelecimentoRequest("estabelecimento");
        webTestClient
                .post()
                .uri("/qualquer")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(estabelecimentoRequest))
                .exchange()
                .expectStatus()
                .isNotFound()
                .expectBody()
                .consumeWith(entityExchangeResult -> assertThat(entityExchangeResult.getResponseBody())
                        .asString()
                        .contains("404", "NOT_FOUND", "/qualquer"));
    }


}
