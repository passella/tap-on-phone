package br.com.passella.cadastro.entrypoint.controller.converter;


import br.com.passella.cadastro.domain.model.EstabelecimentoResponse;
import br.com.passella.cadastro.usecase.CadastrarEstabelecimento.CadastrarEstabelecimentoOutput;
import org.springframework.stereotype.Component;

@Component
public class EstabelecimentoControllerConverter {
    public EstabelecimentoResponse toEstabelecimentoResponse(final CadastrarEstabelecimentoOutput output) {
        return new EstabelecimentoResponse(output.id(), output.nome());
    }
}
