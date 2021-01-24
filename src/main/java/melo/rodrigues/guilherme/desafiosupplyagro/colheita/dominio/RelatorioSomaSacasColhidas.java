package melo.rodrigues.guilherme.desafiosupplyagro.colheita.dominio;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Getter
@RequiredArgsConstructor
public class RelatorioSomaSacasColhidas {
    private final String fazenda;
    private final BigDecimal sacasColhidas;
}
