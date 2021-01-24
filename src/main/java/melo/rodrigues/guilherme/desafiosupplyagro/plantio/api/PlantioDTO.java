package melo.rodrigues.guilherme.desafiosupplyagro.plantio.api;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import melo.rodrigues.guilherme.desafiosupplyagro.plantio.dominio.Plantio;
import melo.rodrigues.guilherme.desafiosupplyagro.talhao.api.TalhaoDTO;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PlantioDTO {

    private final UUID id;
    private final TalhaoDTO talhao;
    private final BigDecimal areaEmHectare;
    private final String variedade;
    private final ZonedDateTime data;

    public static PlantioDTO from(Plantio plantio) {
        return new PlantioDTO(plantio.getId(),
                              TalhaoDTO.from(plantio.getTalhao()),
                              plantio.getAreaPlantadaEmHectare(),
                              plantio.getVariedade(),
                              plantio.getData());
    }
}
