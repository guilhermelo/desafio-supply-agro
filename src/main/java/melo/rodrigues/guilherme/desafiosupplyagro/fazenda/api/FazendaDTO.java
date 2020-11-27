package melo.rodrigues.guilherme.desafiosupplyagro.fazenda.api;

import lombok.Getter;
import melo.rodrigues.guilherme.desafiosupplyagro.fazenda.dominio.Fazenda;

import java.util.UUID;

@Getter
public class FazendaDTO {
    private UUID id;
    private String nome;
    private String cnpj;
    private String estado;
    private String cidade;
    private String logradouro;

    private FazendaDTO(Fazenda fazenda) {
        this.id = fazenda.getId();
        this.nome = fazenda.getNome();
        this.cnpj = fazenda.getCnpj();
        this.estado = fazenda.getEndereco().getEstado();
        this.cidade = fazenda.getEndereco().getCidade();
        this.logradouro = fazenda.getEndereco().getLogradouro();
    }

    public static FazendaDTO from(Fazenda fazenda) {
        return new FazendaDTO(fazenda);
    }
}
