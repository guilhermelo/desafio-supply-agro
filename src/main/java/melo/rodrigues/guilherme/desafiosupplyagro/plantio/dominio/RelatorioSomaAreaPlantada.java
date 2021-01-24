package melo.rodrigues.guilherme.desafiosupplyagro.plantio.dominio;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Getter
@RequiredArgsConstructor
public class RelatorioSomaAreaPlantada {
    private final String fazenda;
    private final String variedade;
    private final BigDecimal somaAreaPlantada;
}
