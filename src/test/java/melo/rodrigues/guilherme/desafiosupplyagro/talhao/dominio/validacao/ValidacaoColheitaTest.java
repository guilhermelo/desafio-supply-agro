package melo.rodrigues.guilherme.desafiosupplyagro.talhao.dominio.validacao;


import melo.rodrigues.guilherme.desafiosupplyagro.colheita.dominio.Colheita;
import melo.rodrigues.guilherme.desafiosupplyagro.fazenda.dominio.Endereco;
import melo.rodrigues.guilherme.desafiosupplyagro.fazenda.dominio.Fazenda;
import melo.rodrigues.guilherme.desafiosupplyagro.plantio.dominio.Plantio;
import melo.rodrigues.guilherme.desafiosupplyagro.talhao.api.exceptions.OperacaoInvalidaException;
import melo.rodrigues.guilherme.desafiosupplyagro.talhao.dominio.Talhao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ValidacaoColheitaTest {

    Talhao talhao;

    @BeforeEach
    void setup() {
        Endereco endereco = Endereco.comCidadeEstadoLogradouro("Assis", "SP", "Rua 1");
        Fazenda fazenda = Fazenda.comNomeCNPJEndereco("Fazenda 1", "05.961.775/0001-47", endereco);

         talhao = Talhao.builder()
                        .fazenda(fazenda)
                        .safra(2020)
                        .areaEmHectare(new BigDecimal("100"))
                        .estimativaProdutividade(new BigDecimal("1000"))
                        .build();
    }

    @Test
    @DisplayName("Lança exceção ao tentar encerrar talhão já encerrado")
    void deveLancarExcecaoTalhaoJaEncerrado() {

        Plantio plantio = Plantio.builder()
                                 .areaPlantadaEmHectare(new BigDecimal("100"))
                                 .data(ZonedDateTime.now())
                                 .variedade("SOJA")
                                 .build();

        Colheita colheita = Colheita.builder()
                                    .data(ZonedDateTime.now())
                                    .areaColhidaEmHectare(new BigDecimal("100"))
                                    .pesoColhidoEmKg(new BigDecimal("900"))
                                    .build();

        talhao.adicionarPlantio(plantio);
        talhao.adicionarColheita(colheita);

        talhao.encerrar();

        assertThrows(OperacaoInvalidaException.class, () ->
                                new ValidacaoColheita().valida(talhao, colheita.gerarEvento()), "Talhão já encerrado!");
    }


    @Test
    @DisplayName("Lança exceção quando a área de colheita é maior que a área do talhão ")
    void deveLancarExcecaoAreaColheitaMaiorAreaTalhao() {

        Colheita colheita = Colheita.builder()
                                    .data(ZonedDateTime.now())
                                    .areaColhidaEmHectare(new BigDecimal("101"))
                                    .pesoColhidoEmKg(new BigDecimal("900"))
                                    .build();

        assertThrows(OperacaoInvalidaException.class,
                                () -> new ValidacaoColheita().valida(talhao, colheita.gerarEvento()),
                "Área de colheita não pode ser maior que a área do talhão");
    }

    @Test
    @DisplayName("Lança exceção quando a soma da área dos eventos do mesmo tipo é maior que área do talhão")
    void deveLancarExcecaoQuandoSomaAreaEventosDoMesmoTipoMaiorAreaTalhao() {


        Colheita colheita = Colheita.builder()
                                    .data(ZonedDateTime.now())
                                    .areaColhidaEmHectare(new BigDecimal("50"))
                                    .pesoColhidoEmKg(new BigDecimal("900"))
                                    .build();

        talhao.adicionarColheita(colheita);

        Colheita colheitaDois = Colheita.builder()
                                        .data(ZonedDateTime.now())
                                        .areaColhidaEmHectare(new BigDecimal("51"))
                                        .pesoColhidoEmKg(new BigDecimal("900"))
                                        .build();

        assertThrows(OperacaoInvalidaException.class,
                () -> new ValidacaoColheita().valida(talhao, colheitaDois.gerarEvento()),
                "A soma da área de colheita não pode ser maior que a área do talhão");

    }

}