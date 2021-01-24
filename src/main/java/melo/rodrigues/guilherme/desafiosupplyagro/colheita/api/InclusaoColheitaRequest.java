package melo.rodrigues.guilherme.desafiosupplyagro.colheita.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import melo.rodrigues.guilherme.desafiosupplyagro.colheita.dominio.Colheita;
import melo.rodrigues.guilherme.desafiosupplyagro.talhao.dominio.Talhao;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class InclusaoColheitaRequest {

    @NotNull(message = "{InclusaoColheitaRequest.pesoColhido.NotNull}")
    @Positive(message = "{InclusaoColheitaRequest.pesoColhido.Positive}")
    private BigDecimal pesoColhidoEmKg;

    @NotNull(message = "{Validation.areaEmHectare.NotNull}")
    @Positive(message = "{Validation.areaEmHectare.Positive}")
    private BigDecimal areaColhidaEmHectare;

    @NotNull(message = "{Validation.data.NotNull}")
    private ZonedDateTime data;

    @NotNull(message = "{Validation.talhao.NotNull}")
    private UUID idTalhao;

    public Colheita toModel(Talhao talhao) {
        return Colheita.builder()
                       .areaColhidaEmHectare(areaColhidaEmHectare)
                       .data(data)
                       .talhao(talhao)
                       .pesoColhidoEmKg(pesoColhidoEmKg)
                       .build();
    }
}
