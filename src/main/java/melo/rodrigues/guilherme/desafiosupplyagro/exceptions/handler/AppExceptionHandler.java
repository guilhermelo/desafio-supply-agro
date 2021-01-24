package melo.rodrigues.guilherme.desafiosupplyagro.exceptions.handler;

import com.totvs.tjf.api.context.v2.response.ApiErrorResponse;
import com.totvs.tjf.api.context.v2.response.ApiErrorResponseDetail;
import com.totvs.tjf.i18n.I18nService;
import melo.rodrigues.guilherme.desafiosupplyagro.talhao.api.exceptions.OperacaoInvalidaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestControllerAdvice
public class AppExceptionHandler {

    @Autowired
    private I18nService i18nService;

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiErrorResponse handleValidacoes(MethodArgumentNotValidException e) {

        List<ApiErrorResponseDetail> detalhes = new ArrayList<>();

        for (ObjectError erro : e.getBindingResult().getAllErrors()) {
            detalhes.add(new ApiErrorResponseDetail(erro.getCode(), erro.getDefaultMessage(), null, null));
        }

        final String mensagem = i18nService.getMessage("mensagem.erro.header");
        final String mensagemDetalhe = i18nService.getMessage("mensagem.erro.detalhe");

        return new ApiErrorResponse("ErroValidacao", mensagem, mensagemDetalhe, "", Collections.unmodifiableList(detalhes));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(OperacaoInvalidaException.class)
    public ApiErrorResponse handleOperacoes(OperacaoInvalidaException e) {
        return new ApiErrorResponse("ErroOperacao", "Operação inválida!", e.getMensagem(), "", Collections.emptyList());
    }
}
