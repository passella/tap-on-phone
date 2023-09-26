package br.com.passella.motorpagamento;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest
@ImportTestcontainers
class MotorPagamentoApplicationTests {

	private static KafkaContainer kafkaContainer;

	@BeforeAll
	static void beforeAll() {
		kafkaContainer = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:6.2.1"));
		kafkaContainer.start();
	}

	@DynamicPropertySource
	static void configureProperties(final DynamicPropertyRegistry registry) {
		registry.add("spring.kafka.bootstrap-servers", kafkaContainer::getBootstrapServers);
	}
	@Test
	void contextLoads() {
	}

}
