package melo.rodrigues.guilherme.desafiosupplyagro.talhao.dominio;

import melo.rodrigues.guilherme.desafiosupplyagro.talhao.dominio.validacao.ValidacaoColheita;
import melo.rodrigues.guilherme.desafiosupplyagro.talhao.dominio.validacao.ValidacaoEncerramento;
import melo.rodrigues.guilherme.desafiosupplyagro.talhao.dominio.validacao.ValidacaoEvento;
import melo.rodrigues.guilherme.desafiosupplyagro.talhao.dominio.validacao.ValidacaoPlantio;

public enum TipoEvento {
    PLANTIO(new ValidacaoPlantio()),
    COLHEITA(new ValidacaoColheita()),
    ENCERRAMENTO(new ValidacaoEncerramento());

    private ValidacaoEvento validacaoEvento;

    TipoEvento(ValidacaoEvento validacaoEvento) {
        this.validacaoEvento = validacaoEvento;
    }

    public void realizaValidacoes(Talhao talhao, Evento evento) {
        this.validacaoEvento.valida(talhao, evento);
    }
}
