package melo.rodrigues.guilherme.desafiosupplyagro.talhao.api;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import melo.rodrigues.guilherme.desafiosupplyagro.talhao.dominio.Talhao;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TalhaoDTO {
    private final UUID id;
    private final String codigo;
    private final BigDecimal areaEmHectare;
    private final Integer safra;
    private final BigDecimal estimativaProdutividade;
    private final UUID idFazenda;

    public static TalhaoDTO from(Talhao talhao) {
        return new TalhaoDTO(talhao.getId(), talhao.getCodigo(), talhao.getAreaEmHectare(), talhao.getSafra(),
                            talhao.getEstimativaProdutividade(), talhao.getFazenda().getId());
    }
}
