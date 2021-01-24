package melo.rodrigues.guilherme.desafiosupplyagro.talhao.dominio.validacao;

import melo.rodrigues.guilherme.desafiosupplyagro.talhao.dominio.Evento;
import melo.rodrigues.guilherme.desafiosupplyagro.talhao.dominio.Talhao;
import melo.rodrigues.guilherme.desafiosupplyagro.talhao.api.exceptions.OperacaoInvalidaException;

public class ValidacaoPlantio implements ValidacaoEvento {

    @Override
    public void valida(Talhao talhao, Evento evento) {

        if (talhao.encerrado()) {
            throw new OperacaoInvalidaException("Talhão já encerrado!");
        }

        if (evento.getAreaEmHectare().compareTo(talhao.getAreaEmHectare()) > 0) {
            throw new OperacaoInvalidaException("Área de plantio não pode ser maior que a área do talhão");
        }

        if(talhao.isSomaAreaEventosMaiorQueAreaTalhao(evento)) {
            throw new OperacaoInvalidaException("A soma da área de plantio é maior que a área do talhão");
        }

    }
}
