package melo.rodrigues.guilherme.desafiosupplyagro.talhao.dominio;

import lombok.*;

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
public class Evento {

    @Id
    @GeneratedValue
    private UUID id;

    @NotNull
    private ZonedDateTime data;

    @NotNull
    @Positive
    private BigDecimal areaEmHectare;

    @Enumerated(EnumType.STRING)
    private TipoEvento tipo;

    @Setter
    @JoinColumn(name = "talhao_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Talhao talhao;

    public void realizaValidacoes(Talhao talhao) {
        this.tipo.realizaValidacoes(talhao, this);
    }
}
