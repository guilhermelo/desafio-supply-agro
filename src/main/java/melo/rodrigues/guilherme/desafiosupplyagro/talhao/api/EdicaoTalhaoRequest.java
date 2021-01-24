package melo.rodrigues.guilherme.desafiosupplyagro.talhao.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EdicaoTalhaoRequest {

    @NotNull(message = "{Validation.areaEmHectare.NotNull}")
    private BigDecimal areaEmHectare;

    @NotNull(message = "{Validation.estimativaProdutividade.NotNull}")
    @Positive(message = "{Validation.estimativaProdutividade.Positive}")
    private BigDecimal estimativaProdutividade;
}
