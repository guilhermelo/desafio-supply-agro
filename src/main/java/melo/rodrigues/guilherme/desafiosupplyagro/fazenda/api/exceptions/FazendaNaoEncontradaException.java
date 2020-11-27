package melo.rodrigues.guilherme.desafiosupplyagro.fazenda.api.exceptions;

import com.totvs.tjf.api.context.stereotype.ApiErrorParameter;
import com.totvs.tjf.api.context.stereotype.error.ApiBadRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@ApiBadRequest
@AllArgsConstructor
public class FazendaNaoEncontradaException extends RuntimeException {

    @ApiErrorParameter
    private String id;
}
