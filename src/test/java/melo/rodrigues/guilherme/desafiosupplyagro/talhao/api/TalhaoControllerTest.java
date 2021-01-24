package melo.rodrigues.guilherme.desafiosupplyagro.talhao.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import melo.rodrigues.guilherme.desafiosupplyagro.ApplicationTests;
import melo.rodrigues.guilherme.desafiosupplyagro.talhao.dominio.Talhao;
import melo.rodrigues.guilherme.desafiosupplyagro.talhao.dominio.TalhaoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = ApplicationTests.class)
@Sql(value = "classpath:scripts/insere_fazenda_talhao_evento.sql")
class TalhaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private TalhaoRepository repository;

    private static final String API = "/api/v1/talhoes";

    @Test
    @DisplayName("Deve incluir talhao")
    void deveIncluirTalhao() throws Exception {
        UUID idFazenda = UUID.fromString("599b2a61-03e4-4738-a9b3-dc02de03ccff");

        InclusaoTalhaoRequest request = new InclusaoTalhaoRequest("TAL3", new BigDecimal(50), 2020, new BigDecimal("150"), idFazenda);

        MockHttpServletRequestBuilder post = post(API).locale(new Locale("pt", "BR"))
                                                      .contentType(MediaType.APPLICATION_JSON)
                                                      .content(mapper.writeValueAsString(request));

        mockMvc.perform(post).andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Nao deve incluir talhão com fazenda inválida")
    void naoDeveIncluirTalhaoComFazendaQueNaoExiste() throws Exception {
        UUID idFazenda = UUID.fromString("1385f7be-d9bf-4ced-9164-424ce6a4d878");

        InclusaoTalhaoRequest request = new InclusaoTalhaoRequest("TAL2", new BigDecimal(50), 2020, new BigDecimal("150"), idFazenda);

        MockHttpServletRequestBuilder post = post(API).locale(new Locale("pt", "BR"))
                                                      .contentType(MediaType.APPLICATION_JSON)
                                                      .content(mapper.writeValueAsString(request));
        mockMvc.perform(post)
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.message").value("Fazenda nao encontrada com id " + idFazenda.toString()));
    }

    @Test
    @DisplayName("Nao deve incluir talhão com código já utilizado por outro talhão da mesma fazenda")
    void naoDeveIncluirTalhaoComCodigoExistenteEmOutroTalhao() throws Exception {
        UUID idFazenda = UUID.fromString("e38e7b36-f52f-4048-ad9d-fa32e35c20c1");

        InclusaoTalhaoRequest request = new InclusaoTalhaoRequest("TAL1", new BigDecimal(50), 2020, new BigDecimal("150"), idFazenda);

        MockHttpServletRequestBuilder post = post(API).locale(new Locale("pt", "BR"))
                                                      .contentType(MediaType.APPLICATION_JSON)
                                                      .content(mapper.writeValueAsString(request));
        mockMvc.perform(post)
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.message").value("Codigo ja utilizado por outro talhao da mesma fazenda"));
    }

    @Test
    @DisplayName("Deve alterar a área e a estimativa de produtividade do talhão")
    void deveAlterarAreaEstimativaTalhao() throws Exception {

        String idTalhao = "e5b3d4f6-023f-4bf5-ad54-9e6449138aed";

        EdicaoTalhaoRequest request = new EdicaoTalhaoRequest(new BigDecimal(10), new BigDecimal("20"));

        MockHttpServletRequestBuilder put = put(API + "/" + idTalhao).locale(new Locale("pt", "BR"))
                                                                     .contentType(MediaType.APPLICATION_JSON)
                                                                     .content(mapper.writeValueAsString(request));

        mockMvc.perform(put).andExpect(status().isOk());
        Optional<Talhao> talhaoOpcional = repository.findById(UUID.fromString(idTalhao));
        talhaoOpcional.ifPresent((talhao) -> {
            Assertions.assertEquals(request.getAreaEmHectare(), talhao.getAreaEmHectare());
            Assertions.assertEquals(request.getEstimativaProdutividade(), talhao.getEstimativaProdutividade());
        });
    }

    @Test
    @DisplayName("Não deve alterar a área e a estimativa de produtividade do talhão quando talhão não existir")
    void naoDeveAlterarAreaEstimativaQuandoNaoExisteTalhao() throws Exception {

        String idTalhao = "a1e3fc0e-6be6-43e3-a6b0-43d5f0aafca2";

        EdicaoTalhaoRequest request = new EdicaoTalhaoRequest(new BigDecimal(10), new BigDecimal("20"));

        MockHttpServletRequestBuilder put = put(API + "/" + idTalhao).locale(new Locale("pt", "BR"))
                                                                     .contentType(MediaType.APPLICATION_JSON)
                                                                     .content(mapper.writeValueAsString(request));

        mockMvc.perform(put)
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.message").value("Talhao nao encontrado com id " + idTalhao));

    }

    @Test
    @DisplayName("Deve buscar todos os talhões")
    void deveBuscarTodosTalhoes() throws Exception {
        MockHttpServletRequestBuilder get = get(API).locale(new Locale("pt", "BR"))
                                                    .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(get)
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.items", hasSize(6)));
    }

    @Test
    @DisplayName("Deve buscar talhão por id")
    void deveBuscarTalhaoPorId() throws Exception {
        String idTalhao = "51e375de-3054-499b-8002-ba94db711681";
        MockHttpServletRequestBuilder get = get(API + "/" + idTalhao).locale(new Locale("pt", "BR"))
                                                                     .contentType(MediaType.APPLICATION_JSON);

        MvcResult resultado = mockMvc.perform(get).andExpect(status().isOk()).andReturn();

        TalhaoDTO talhaoRecuperado = mapper.readValue(resultado.getResponse().getContentAsString(), TalhaoDTO.class);

        Assertions.assertNotNull(talhaoRecuperado);
        Assertions.assertEquals("TAL1", talhaoRecuperado.getCodigo());
    }

    @Test
    @DisplayName("Não deve encontrar talhão ao buscar por id inexistente")
    void naoDeveEncontrarTalhaoPorId() throws Exception {
        String idTalhao = "274b9e9a-9d77-41e5-a542-d3306afd167a";
        MockHttpServletRequestBuilder get = get(API + "/" + idTalhao).locale(new Locale("pt", "BR"))
                                                                     .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(get).andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve excluir talhao por id")
    void deveExcluirTalhaoPorId() throws Exception {

        String idTalhao = "63d81d90-c185-4dc0-844b-3d881157eff9";

        MockHttpServletRequestBuilder delete = delete(API + "/" + idTalhao).locale(new Locale("pt", "BR"))
                                                                           .contentType(MediaType.APPLICATION_JSON);


        mockMvc.perform(delete).andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Não deve excluir talhão ao buscar por id inexistente")
    void naoDeveExcluirTalhaoPorId() throws Exception {
        String idTalhao = "274b9e9a-9d77-41e5-a542-d3306afd167a";
        MockHttpServletRequestBuilder delete = delete(API + "/" + idTalhao).locale(new Locale("pt", "BR"))
                                                                           .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(delete)
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.message").value("Talhao nao encontrado com id " + idTalhao));
    }

    @Test
    @DisplayName("Deve recuperar os eventos do talhão de acordo com id informado")
    void deveRecuperarEventosDoTalhao() throws Exception {
        String idTalhao = "63d81d90-c185-4dc0-844b-3d881157eff9";

        final String uri = API + "/" + idTalhao + "/eventos";
        MockHttpServletRequestBuilder get = get(uri).locale(new Locale("pt", "BR"))
                                                    .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(get)
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.items", hasSize(2)));
    }

    @Test
    @DisplayName("Deve encerrar talhão")
    void deveEncerrarTalhao() throws Exception {
        final String uri = API + "/encerra/2020";

        MockHttpServletRequestBuilder put = put(uri).locale(new Locale("pt", "BR"))
                                                    .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(put)
               .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Não deve encerrar talhão quando a estimativa de produtividade não foi alcançada")
    void naoDeveEncerrarTalhaoComEstimativaProdutividadeNaoAlcancada() throws Exception {
        final String uri = API + "/encerra/2021";

        MockHttpServletRequestBuilder put = put(uri).locale(new Locale("pt", "BR"))
                                                    .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(put)
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.detailedMessage").value("Talhão não pode ser encerrado. Percentual mínimo de produtividade não alcançado"));
    }
}
