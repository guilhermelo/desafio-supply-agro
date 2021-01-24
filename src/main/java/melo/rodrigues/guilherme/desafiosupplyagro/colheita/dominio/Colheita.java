package melo.rodrigues.guilherme.desafiosupplyagro.colheita.dominio;

import lombok.*;
import melo.rodrigues.guilherme.desafiosupplyagro.talhao.dominio.GeradorEventos;
import melo.rodrigues.guilherme.desafiosupplyagro.talhao.dominio.Evento;
import melo.rodrigues.guilherme.desafiosupplyagro.talhao.dominio.Talhao;
import melo.rodrigues.guilherme.desafiosupplyagro.talhao.dominio.TipoEvento;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class Colheita implements GeradorEventos {

    @Id
    @GeneratedValue
    private UUID id;

    @NotNull
    @Positive
    private BigDecimal pesoColhidoEmKg;

    @NotNull
    @Positive
    private BigDecimal areaColhidaEmHectare;

    @NotNull
    private ZonedDateTime data;

    @Setter
    @NotNull
    @JoinColumn(name = "talhao_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Talhao talhao;

    @Override
    public Evento gerarEvento() {
        return Evento.builder()
                     .areaEmHectare(areaColhidaEmHectare)
                     .data(data)
                     .tipo(TipoEvento.COLHEITA)
                     .talhao(talhao)
                     .build();
    }
}
