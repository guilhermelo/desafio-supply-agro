package melo.rodrigues.guilherme.desafiosupplyagro.talhao.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import melo.rodrigues.guilherme.desafiosupplyagro.fazenda.dominio.Fazenda;
import melo.rodrigues.guilherme.desafiosupplyagro.talhao.dominio.Talhao;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class InclusaoTalhaoRequest {

    @Size(max = 10, message = "{Validation.codigo.Size}")
    @NotNull(message = "{Validation.codigo.NotNull}")
    private String codigo;

    @NotNull(message = "{InclusaoTalhaoRequest.areaEmHectare.NotNull}")
    private BigDecimal areaEmHectare;

    @NotNull(message = "{Validation.safra.NotNull}")
    private Integer safra;

    @NotNull(message = "{InclusaoTalhaoRequest.estimativaProdutividade.NotNull}")
    @Positive(message = "{Validation.estimativaProdutividade.Positive}")
    private BigDecimal estimativaProdutividade;

    @NotNull(message = "{Validation.fazenda.NotNull}")
    private UUID idFazenda;

    public Talhao toModel(Fazenda fazenda) {
        return Talhao.builder()
                     .codigo(codigo)
                     .areaEmHectare(areaEmHectare)
                     .safra(safra)
                     .estimativaProdutividade(estimativaProdutividade)
                     .fazenda(fazenda)
                     .build();

    }
}
