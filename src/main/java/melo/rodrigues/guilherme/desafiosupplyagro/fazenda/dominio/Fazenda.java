package melo.rodrigues.guilherme.desafiosupplyagro.fazenda.dominio;

import lombok.*;
import org.hibernate.validator.constraints.br.CNPJ;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Fazenda {

    @Id
    @GeneratedValue
    private UUID id;

    @NotNull
    @Size(max = 200)
    @Column(unique = true)
    private String nome;

    @CNPJ
    @NotNull
    private String cnpj;

    @Embedded
    private Endereco endereco;

    private Fazenda(String nome, String cnpj, Endereco endereco) {
        this.nome = nome;
        this.cnpj = cnpj;
        this.endereco = endereco;
    }

    public Fazenda atualizaNome(@NonNull String nome) {
        return new Fazenda(id, nome, cnpj, endereco);
    }

    public static Fazenda comNomeCNPJEndereco(String nome, String cnpj, Endereco endereco) {
        return new Fazenda(nome, cnpj, endereco);
    }
}
