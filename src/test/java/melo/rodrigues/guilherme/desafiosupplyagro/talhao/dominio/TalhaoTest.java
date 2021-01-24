package melo.rodrigues.guilherme.desafiosupplyagro.talhao.dominio;

import melo.rodrigues.guilherme.desafiosupplyagro.colheita.dominio.Colheita;
import melo.rodrigues.guilherme.desafiosupplyagro.fazenda.dominio.Endereco;
import melo.rodrigues.guilherme.desafiosupplyagro.fazenda.dominio.Fazenda;
import melo.rodrigues.guilherme.desafiosupplyagro.plantio.dominio.Plantio;
import melo.rodrigues.guilherme.desafiosupplyagro.talhao.api.exceptions.OperacaoInvalidaException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TalhaoTest {
    private Talhao talhao;

    @BeforeEach
    void setup() {
        Endereco endereco = Endereco.comCidadeEstadoLogradouro("Assis", "SP", "Rua 1");
        talhao = Talhao.builder()
                       .areaEmHectare(new BigDecimal("400"))
                       .codigo("TAL1")
                       .estimativaProdutividade(new BigDecimal("300"))
                       .fazenda(Fazenda.comNomeCNPJEndereco("Fazenda 1", "17.094.456/0001-60", endereco))
                       .safra(2020)
                       .build();
    }

    @Test
    @DisplayName("Deve adicionar um evento de plantio ao talhão")
    void deveAdicionarEventoPlantioAoTalhao() {
        Plantio plantio = Plantio.builder()
                                 .data(ZonedDateTime.now())
                                 .variedade("SOJA")
                                 .areaPlantadaEmHectare(new BigDecimal("20"))
                                 .build();

        talhao.adicionarPlantio(plantio);

        assertEquals(1, talhao.getEventos().size());
    }

    @Test
    @DisplayName("Lança exceção quando área total de eventos do mesmo tipo é maior que área do talhão")
    void deveLancarExcecaoQuandoTamanhoDaAreaDeEventosComMesmoTipoEhMaiorAreaTalhao() {

        Plantio plantio = Plantio.builder()
                                 .areaPlantadaEmHectare(new BigDecimal("300"))
                                 .variedade("SOJA")
                                 .data(ZonedDateTime.now())
                                 .build();


        Colheita colheita = Colheita.builder()
                                    .areaColhidaEmHectare(new BigDecimal("110"))
                                    .pesoColhidoEmKg(new BigDecimal("50"))
                                    .data(ZonedDateTime.now())
                                    .build();

        Plantio plantioDois = Plantio.builder()
                                     .areaPlantadaEmHectare(new BigDecimal("110"))
                                     .variedade("SOJA")
                                     .data(ZonedDateTime.now())
                                     .build();

        talhao.adicionarPlantio(plantio);
        talhao.adicionarColheita(colheita);

        assertThrows(OperacaoInvalidaException.class, () -> talhao.adicionarPlantio(plantioDois));
    }

    @Test
    @DisplayName("Deve lançar exceção quando já existe evento de encerramento ao adiciona-lo")
    void deveLancarExcecaoQuandoJaExisteEventoDeEncerramentoAoAdicionar() {

        Plantio plantio = Plantio.builder()
                                 .areaPlantadaEmHectare(new BigDecimal("400"))
                                 .data(ZonedDateTime.now())
                                 .variedade("SOJA")
                                 .build();

        Colheita colheita = Colheita.builder()
                                    .areaColhidaEmHectare(new BigDecimal("300"))
                                    .data(ZonedDateTime.now())
                                    .pesoColhidoEmKg(new BigDecimal("295"))
                                    .build();

        talhao.adicionarPlantio(plantio);
        talhao.adicionarColheita(colheita);
        talhao.encerrar();

        assertThrows(OperacaoInvalidaException.class, () -> talhao.encerrar());
    }

    @Test
    @DisplayName("Deve atualizar área e estimativa de produção")
    void deveAtualizarTalhao() {
        Talhao talhaoAtualizado = this.talhao.atualizaAreaEstimativa(new BigDecimal("60"), new BigDecimal("300"));

        assertEquals(new BigDecimal("60"), talhaoAtualizado.getAreaEmHectare());
        assertEquals(new BigDecimal("300"), talhaoAtualizado.getEstimativaProdutividade());
    }

    @Test
    @DisplayName("Deve atualizar área e estimativa de produção")
    void naoDeveAtualizarTalhaoQuandoTalhaoTemEventos() {
        Plantio plantio = Plantio.builder()
                                 .areaPlantadaEmHectare(new BigDecimal("20"))
                                 .data(ZonedDateTime.now())
                                 .variedade("SOJA")
                                 .build();

        talhao.adicionarPlantio(plantio);

        BigDecimal areaEmHectare = new BigDecimal("60");
        BigDecimal estimativaProdutividade = new BigDecimal("300");
        assertThrows(OperacaoInvalidaException.class, () -> talhao.atualizaAreaEstimativa(areaEmHectare, estimativaProdutividade));
    }

    @Test
    @DisplayName("Deve encerrar talhão")
    void deveEncerrarTalhao() {

        Plantio plantio = Plantio.builder()
                                 .variedade("SOJA")
                                 .data(ZonedDateTime.now())
                                 .areaPlantadaEmHectare(new BigDecimal("290"))
                                 .build();

        Colheita colheita = Colheita.builder()
                                    .areaColhidaEmHectare(new BigDecimal("275"))
                                    .data(ZonedDateTime.now())
                                    .pesoColhidoEmKg(new BigDecimal("295"))
                                    .build();

        talhao.adicionarPlantio(plantio);
        talhao.adicionarColheita(colheita);

        talhao.encerrar();

        assertTrue(talhao.encerrado());
    }

    @Test
    @DisplayName("Não deve encerrar talhão quando a estimativa de produtividade está abaixo de 90%")
    void naoDeveEncerrarTalhao() {

        Plantio plantio = Plantio.builder()
                                 .areaPlantadaEmHectare(new BigDecimal("50"))
                                 .data(ZonedDateTime.now())
                                 .variedade("SOJA")
                                 .build();

        Colheita colheita = Colheita.builder()
                                    .pesoColhidoEmKg(new BigDecimal("269"))
                                    .areaColhidaEmHectare(new BigDecimal("50"))
                                    .data(ZonedDateTime.now())
                                    .build();

        talhao.adicionarPlantio(plantio);
        talhao.adicionarColheita(colheita);

        assertThrows(OperacaoInvalidaException.class, () -> talhao.encerrar());
    }

    @Test
    @DisplayName("Deve gerar talhão para a próxima safra")
    void deveGerarTalhaoParaProximaSafra() {

        Talhao talhaoParaProximaSafra = this.talhao.geraTalhaoParaProximaSafra();

        assertNotEquals(talhaoParaProximaSafra.getSafra(), talhao.getSafra());
        assertEquals(talhao.getSafra() + 1, talhaoParaProximaSafra.getSafra());
    }
}