package melo.rodrigues.guilherme.desafiosupplyagro.talhao.dominio;

import lombok.*;
import melo.rodrigues.guilherme.desafiosupplyagro.colheita.dominio.Colheita;
import melo.rodrigues.guilherme.desafiosupplyagro.fazenda.dominio.Fazenda;
import melo.rodrigues.guilherme.desafiosupplyagro.plantio.dominio.Plantio;
import melo.rodrigues.guilherme.desafiosupplyagro.talhao.api.exceptions.OperacaoInvalidaException;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Talhao {

    @Id
    @GeneratedValue
    private UUID id;

    @NotNull
    private String codigo;

    @NotNull
    private BigDecimal areaEmHectare;

    @NotNull
    private Integer safra;

    @NotNull
    private BigDecimal estimativaProdutividade;

    @NotNull
    @JoinColumn(name = "fazenda_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Fazenda fazenda;

    @OneToMany(mappedBy = "talhao", orphanRemoval = true, cascade = CascadeType.ALL)
    @Builder.Default
    private final Set<Evento> eventos = new HashSet<>();

    @OneToMany(mappedBy = "talhao", orphanRemoval = true, cascade = CascadeType.ALL)
    @Builder.Default
    private final Set<Colheita> colheitas = new HashSet<>();

    @OneToMany(mappedBy = "talhao", orphanRemoval = true, cascade = CascadeType.ALL)
    @Builder.Default
    private final Set<Plantio> plantios = new HashSet<>();

    private void adicionarEvento(Evento evento) {

        evento.realizaValidacoes(this);

        this.eventos.add(evento);
        evento.setTalhao(this);
    }

    public void adicionarPlantio(Plantio plantio) {
        adicionarEvento(plantio.gerarEvento());

        this.plantios.add(plantio);
        plantio.setTalhao(this);
    }

    public void adicionarColheita(Colheita colheita) {
        adicionarEvento(colheita.gerarEvento());

        this.colheitas.add(colheita);
        colheita.setTalhao(this);
    }

    public boolean encerrado() {
        return this.eventos.stream().anyMatch(e -> e.getTipo().equals(TipoEvento.ENCERRAMENTO));
    }

    public boolean isSomaAreaEventosMaiorQueAreaTalhao(Evento evento) {
        BigDecimal areaTotalEmHectaresPorEvento = areaTotalEmHectaresPorEvento(evento.getTipo());

        return areaTotalEmHectaresPorEvento.add(evento.getAreaEmHectare()).compareTo(areaEmHectare) > 0;
    }

    private BigDecimal areaTotalEmHectaresPorEvento(TipoEvento tipoEvento) {
        return this.eventos.stream().filter(e -> e.getTipo().equals(tipoEvento))
                           .map(Evento::getAreaEmHectare)
                           .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Talhao atualizaAreaEstimativa(BigDecimal areaEmHectare, BigDecimal estimativaProdutividade) {

        if(!this.eventos.isEmpty()) {
            throw new OperacaoInvalidaException("Talhão não pode ser atualizado quando já contém eventos!");
        }

        return Talhao.builder()
                     .id(id)
                     .codigo(codigo)
                     .areaEmHectare(areaEmHectare)
                     .safra(safra)
                     .estimativaProdutividade(estimativaProdutividade)
                     .fazenda(fazenda)
                     .build();
    }

    public void encerrar() {
        Evento encerramento = Evento.builder()
                                    .data(ZonedDateTime.now())
                                    .talhao(this)
                                    .tipo(TipoEvento.ENCERRAMENTO)
                                    .build();

        adicionarEvento(encerramento);
    }

    public Talhao geraTalhaoParaProximaSafra() {
        return Talhao.builder()
                     .codigo(codigo)
                     .areaEmHectare(areaEmHectare)
                     .safra(safra + 1)
                     .estimativaProdutividade(estimativaProdutividade)
                     .fazenda(fazenda)
                     .build();
    }

    public boolean podeSerEncerrado() {
        final BigDecimal percentualMinimoProdutividade = new BigDecimal("90");

        return percentualTotalColhido().compareTo(percentualMinimoProdutividade) >= 0;
    }

    private BigDecimal percentualTotalColhido() {
        final BigDecimal produtividade = colheitas.stream()
                                                  .map(Colheita::getPesoColhidoEmKg)
                                                  .reduce(BigDecimal.ZERO, BigDecimal::add);

        return (produtividade.multiply(new BigDecimal("100"))).divide(estimativaProdutividade, RoundingMode.FLOOR);
    }

    @Override
    public String toString() {
        return "Talhao{" +
                "id=" + id +
                ", codigo='" + codigo + '\'' +
                ", areaEmHectare=" + areaEmHectare +
                ", safra=" + safra +
                ", estimativaProdutividade=" + estimativaProdutividade +
                '}';
    }



}
