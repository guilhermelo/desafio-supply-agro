package melo.rodrigues.guilherme.desafiosupplyagro.plantio.api;

import java.time.ZonedDateTime;
import java.util.UUID;

public interface EventoProjecao {

    UUID getId();
    ZonedDateTime getData();
    UUID getTalhao();
}
