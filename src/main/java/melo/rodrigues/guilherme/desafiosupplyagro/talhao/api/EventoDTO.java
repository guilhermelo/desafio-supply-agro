package melo.rodrigues.guilherme.desafiosupplyagro.talhao.api;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import melo.rodrigues.guilherme.desafiosupplyagro.talhao.dominio.Evento;
import melo.rodrigues.guilherme.desafiosupplyagro.talhao.dominio.TipoEvento;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class EventoDTO {
    private final ZonedDateTime data;
    private final BigDecimal areaEmHectare;
    private final TipoEvento tipo;
    private final TalhaoDTO talhao;

    public static EventoDTO from(Evento evento) {
        return new EventoDTO(evento.getData(), evento.getAreaEmHectare(), evento.getTipo(), TalhaoDTO.from(evento.getTalhao()));
    }
}
