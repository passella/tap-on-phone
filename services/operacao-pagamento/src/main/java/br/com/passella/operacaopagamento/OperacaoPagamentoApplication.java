package br.com.passella.operacaopagamento;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class OperacaoPagamentoApplication {

	public static void main(String[] args) {
		SpringApplication.run(OperacaoPagamentoApplication.class, args);
	}

}
