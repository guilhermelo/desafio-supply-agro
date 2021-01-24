package melo.rodrigues.guilherme.desafiosupplyagro.talhao.dominio.validacao;

import melo.rodrigues.guilherme.desafiosupplyagro.talhao.dominio.Evento;
import melo.rodrigues.guilherme.desafiosupplyagro.talhao.dominio.Talhao;
import melo.rodrigues.guilherme.desafiosupplyagro.talhao.api.exceptions.OperacaoInvalidaException;

public class ValidacaoEncerramento implements ValidacaoEvento {
    @Override
    public void valida(Talhao talhao, Evento evento) {

        if(talhao.encerrado()) {
            throw new OperacaoInvalidaException("Talhão já encerrado!");
        }

        if(!talhao.podeSerEncerrado()) {
            throw new OperacaoInvalidaException("Talhão não pode ser encerrado. Percentual mínimo de produtividade não alcançado");
        }
    }
}
