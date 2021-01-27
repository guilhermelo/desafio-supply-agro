package melo.rodrigues.guilherme.desafiosupplyagro.plantio.api;

import melo.rodrigues.guilherme.desafiosupplyagro.talhao.api.TalhaoDTO;

import java.time.ZonedDateTime;
import java.util.UUID;

public interface EventoProjecao {

    UUID getId();
    ZonedDateTime getData();
    TalhaoDTO getTalhao();
}
