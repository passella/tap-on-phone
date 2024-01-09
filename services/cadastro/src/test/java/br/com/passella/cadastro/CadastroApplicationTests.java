package br.com.passella.cadastro;

import br.com.passella.cadastro.utils.ContainersForTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

@SpringBootTest
class CadastroApplicationTests {


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

    @Test
    @SuppressWarnings("java:S2699")
    void contextLoads() {
    }

}
