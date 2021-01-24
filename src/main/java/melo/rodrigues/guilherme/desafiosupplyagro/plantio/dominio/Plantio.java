package melo.rodrigues.guilherme.desafiosupplyagro.plantio.dominio;


import lombok.*;
import melo.rodrigues.guilherme.desafiosupplyagro.talhao.dominio.GeradorEventos;
import melo.rodrigues.guilherme.desafiosupplyagro.talhao.dominio.Evento;
import melo.rodrigues.guilherme.desafiosupplyagro.talhao.dominio.Talhao;
import melo.rodrigues.guilherme.desafiosupplyagro.talhao.dominio.TipoEvento;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
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
public class Plantio implements GeradorEventos {

    @Id
    @GeneratedValue
    private UUID id;

    @Setter
    @NotNull
    @JoinColumn(name = "talhao_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Talhao talhao;

    @NotNull
    @Positive
    private BigDecimal areaPlantadaEmHectare;

    @NotBlank
    private String variedade;

    @NotNull
    private ZonedDateTime data;

    @Override
    public Evento gerarEvento() {
        return Evento.builder()
                     .talhao(talhao)
                     .tipo(TipoEvento.PLANTIO)
                     .data(data)
                     .areaEmHectare(areaPlantadaEmHectare)
                     .build();
    }
}
