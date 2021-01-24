package melo.rodrigues.guilherme.desafiosupplyagro.plantio.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import melo.rodrigues.guilherme.desafiosupplyagro.plantio.dominio.Plantio;
import melo.rodrigues.guilherme.desafiosupplyagro.talhao.dominio.Talhao;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InclusaoPlantioRequest {

    @NotNull(message = "{Validation.talhao.NotNull}")
    private UUID idTalhao;

    @NotNull(message = "{Validation.areaEmHectare.NotNull}")
    @Positive(message = "{Validation.areaEmHectare.Positive}")
    private BigDecimal areaEmHectare;

    @NotBlank(message = "{InclusaoPlantioRequest.variedade.NotBlank}")
    private String variedade;

    @NotNull(message = "{Validation.data.NotNull}")
    private ZonedDateTime data;

    public Plantio toModel(Talhao talhao) {
        return Plantio.builder()
                      .areaPlantadaEmHectare(areaEmHectare)
                      .talhao(talhao)
                      .data(data)
                      .variedade(variedade)
                      .build();
    }
}
