package br.com.passella.cadastro;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(
        info = @Info(
                title = "Cadastro de Estabelecimentos",
                description = "API para cadastro de usuários",
                version = "${application.version}"
        )
)
@SpringBootApplication
public class CadastroApplication {


    public static void main(final String[] args) {
        SpringApplication.run(CadastroApplication.class, args);
    }


}
