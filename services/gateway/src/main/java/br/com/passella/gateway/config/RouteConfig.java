package br.com.passella.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfig {

    @Value(value = "${rotas.cadastro}")
    private String cadastro;

    @Value(value = "${rotas.operacao-pagamento}")
    private String operacaoPagamento;

    @Value(value = "${rotas.pagamento}")
    private String pagamento;
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("cadastro", r -> r.path("/cadastro/**")
                        .filters(f -> f.stripPrefix(1))
                        .uri(cadastro))
                .route("operacao-pagamento", r -> r.path("/operacao-pagamento/**")
                        .filters(f -> f.stripPrefix(1))
                        .uri(operacaoPagamento))
                .route("pagamento", r -> r.path("/pagamento/**")
                        .filters(f -> f.stripPrefix(1))
                        .uri(pagamento))
                .build();
    }
}
