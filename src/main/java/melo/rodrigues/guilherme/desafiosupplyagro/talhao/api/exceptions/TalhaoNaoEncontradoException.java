package melo.rodrigues.guilherme.desafiosupplyagro.talhao.api.exceptions;

import com.totvs.tjf.api.context.stereotype.ApiErrorParameter;
import com.totvs.tjf.api.context.stereotype.error.ApiBadRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@ApiBadRequest
@AllArgsConstructor
public class TalhaoNaoEncontradoException extends RuntimeException {

    @ApiErrorParameter
    private final String id;
}
