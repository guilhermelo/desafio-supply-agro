package melo.rodrigues.guilherme.desafiosupplyagro.fazenda.dominio;

import lombok.Getter;

import javax.persistence.Embeddable;
import java.util.Objects;

@Getter
@Embeddable
public class Endereco {

    private String cidade;
    private String estado;
    private String logradouro;

    private Endereco() {
    }

    private Endereco(String cidade, String estado, String logradouro) {
        this.cidade = cidade;
        this.estado = estado;
        this.logradouro = logradouro;
    }

    public static Endereco comCidadeEstadoLogradouro(String cidade, String estado, String logradouro) {
        return new Endereco(cidade, estado, logradouro);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Endereco endereco = (Endereco) o;
        return Objects.equals(cidade, endereco.cidade) &&
                Objects.equals(estado, endereco.estado) &&
                Objects.equals(logradouro, endereco.logradouro);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cidade, estado, logradouro);
    }

    @Override
    public String toString() {
        return "Endereco{" +
                "cidade='" + cidade + '\'' +
                ", estado='" + estado + '\'' +
                ", logradouro='" + logradouro + '\'' +
                '}';
    }
}
