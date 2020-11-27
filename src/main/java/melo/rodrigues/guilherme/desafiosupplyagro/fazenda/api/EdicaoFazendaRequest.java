package melo.rodrigues.guilherme.desafiosupplyagro.fazenda.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EdicaoFazendaRequest {

    @NotNull(message = "{Validation.nome.NotNull}")
    @Size(max = 200, message = "{Validation.nome.Size}")
    private String nome;
}
