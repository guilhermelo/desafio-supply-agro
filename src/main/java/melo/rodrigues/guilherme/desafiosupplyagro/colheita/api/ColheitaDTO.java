package melo.rodrigues.guilherme.desafiosupplyagro.colheita.api;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import melo.rodrigues.guilherme.desafiosupplyagro.colheita.dominio.Colheita;
import melo.rodrigues.guilherme.desafiosupplyagro.talhao.api.TalhaoDTO;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ColheitaDTO {
    private final UUID id;
    private final BigDecimal pesoColhidoEmKg;
    private final BigDecimal areaColhidaEmHectare;
    private final ZonedDateTime data;
    private final TalhaoDTO talhao;

    public static ColheitaDTO from(Colheita colheita) {
        return new ColheitaDTO(colheita.getId(),
                               colheita.getPesoColhidoEmKg(),
                               colheita.getAreaColhidaEmHectare(),
                               colheita.getData(),
                               TalhaoDTO.from(colheita.getTalhao()));
    }
}
