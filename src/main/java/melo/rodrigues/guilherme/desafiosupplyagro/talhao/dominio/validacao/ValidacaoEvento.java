package melo.rodrigues.guilherme.desafiosupplyagro.talhao.dominio.validacao;

import melo.rodrigues.guilherme.desafiosupplyagro.talhao.dominio.Evento;
import melo.rodrigues.guilherme.desafiosupplyagro.talhao.dominio.Talhao;

public interface ValidacaoEvento {
    void valida(Talhao talhao, Evento evento);
}
