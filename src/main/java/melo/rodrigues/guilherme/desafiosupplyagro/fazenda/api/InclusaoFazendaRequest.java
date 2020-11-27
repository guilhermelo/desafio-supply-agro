package melo.rodrigues.guilherme.desafiosupplyagro.fazenda.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import melo.rodrigues.guilherme.desafiosupplyagro.custom.validation.Unico;
import melo.rodrigues.guilherme.desafiosupplyagro.fazenda.dominio.Endereco;
import melo.rodrigues.guilherme.desafiosupplyagro.fazenda.dominio.Fazenda;
import org.hibernate.validator.constraints.br.CNPJ;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class InclusaoFazendaRequest {

    @Unico(entidade = Fazenda.class, coluna="nome", message = "{InclusaoFazendaRequest.nome.Unico}")
    @NotNull(message = "{Validation.nome.NotNull}")
    @Size(max = 200, message = "{Validation.nome.Size}")
    private String nome;

    @Unico(entidade = Fazenda.class, coluna = "cnpj", message = "{InclusaoFazendaRequest.cnpj.Unico}")
    @CNPJ(message = "{InclusaoFazendaRequest.cnpj.CNPJ}")
    @NotNull(message = "{InclusaoFazendaRequest.cnpj.NotNull}")
    private String cnpj;

    @NotNull(message = "{InclusaoFazendaRequest.cidade.NotNull}")
    private String cidade;

    @NotNull(message = "{InclusaoFazendaRequest.estado.NotNull}")
    private String estado;

    @NotNull(message = "{InclusaoFazendaRequest.logradouro.NotNull}")
    private String logradouro;

    public Fazenda toModel() {
        return Fazenda.comNomeCNPJEndereco(nome, cnpj, Endereco.comCidadeEstadoLogradouro(cidade, estado, logradouro));
    }
}
