package melo.rodrigues.guilherme.desafiosupplyagro.plantio.api;

import lombok.RequiredArgsConstructor;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@RequiredArgsConstructor
public class Filtro {
    private final String dataInicial;
    private final String dataFinal;

    private static DateTimeFormatter formatter = DateTimeFormatter.ISO_ZONED_DATE_TIME;

    public ZonedDateTime getDataInicial() {
        return ZonedDateTime.parse(dataInicial, formatter);
    }

    public ZonedDateTime getDataFinal() {
        return ZonedDateTime.parse(dataFinal, formatter);
    }

    public boolean isDatasInformadas() {
        return Objects.nonNull(dataInicial) && Objects.nonNull(dataFinal);
    }
}

