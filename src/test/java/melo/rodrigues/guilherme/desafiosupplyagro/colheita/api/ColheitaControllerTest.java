package melo.rodrigues.guilherme.desafiosupplyagro.colheita.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import melo.rodrigues.guilherme.desafiosupplyagro.ApplicationTests;
import melo.rodrigues.guilherme.desafiosupplyagro.colheita.dominio.RelatorioSomaSacasColhidas;
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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = ApplicationTests.class)
@Sql(value = "classpath:scripts/insere_fazenda_talhao_evento.sql")
class ColheitaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    private static final String API = "/api/v1/colheitas";

    @Test
    @DisplayName("Deve realizar um apontamento de colheita")
    void deveInserirColheita() throws Exception {
        ZonedDateTime hoje = ZonedDateTime.now();
        UUID idTalhao = UUID.fromString("e5b3d4f6-023f-4bf5-ad54-9e6449138aed");
        InclusaoColheitaRequest request = new InclusaoColheitaRequest(new BigDecimal("50000"), new BigDecimal(10), hoje, idTalhao);

        MockHttpServletRequestBuilder post = post(API).locale(new Locale("pt", "BR"))
                                                      .contentType(MediaType.APPLICATION_JSON)
                                                      .content(mapper.writeValueAsString(request));
        mockMvc.perform(post)
               .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Deve buscar o apontamento de colheita pelo id")
    void deveBuscarPorId() throws Exception {
        final String idColheita = "06deb3e2-581c-4706-9504-8a67df76b761";

        MockHttpServletRequestBuilder get = get(API + "/" + idColheita).locale(new Locale("pt", "BR"))
                                                                       .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(get)
               .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Deve buscar colheitas com paginação")
    void deveBuscarColheitasPaginadas() throws Exception {
        MockHttpServletRequestBuilder get = get(API).locale(new Locale("pt", "BR"))
                                                    .contentType(MediaType.APPLICATION_JSON)
                                                    .param("pageSize", "1");

        mockMvc.perform(get)
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.items", hasSize(1)));
    }

    @Test
    @DisplayName("Deve buscar eventos de colheita com paginação")
    void deveBuscarColheitasPaginadasComProjecao() throws Exception {
        MockHttpServletRequestBuilder get = get(API + "/resumo").locale(new Locale("pt", "BR"))
                                                    .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(get)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items", hasSize(5)));
    }

    @Test
    @DisplayName("Deve retornar relatório com a soma das sacas colhidas por fazenda com filtro por data")
    void deveRetornarRelatorioSomaSacasColhidasComFiltroPorData() throws Exception {

        LocalDateTime localDateTime = LocalDateTime.of(2021, Month.JANUARY, 1, 8, 30, 0);

        ZonedDateTime dataInicial = ZonedDateTime.of(localDateTime, ZoneId.systemDefault());
        ZonedDateTime dataFinal = dataInicial.plusDays(16);

        MultiValueMap<String, String> parametros = new LinkedMultiValueMap<>();
        parametros.add("dataInicial", dataInicial.toString());
        parametros.add("dataFinal", dataFinal.toString());

        MockHttpServletRequestBuilder get = get(API + "/relatorio/soma/sacasColhidas").locale(new Locale("pt", "BR"))
                                                                                      .params(parametros)
                                                                                      .contentType(MediaType.APPLICATION_JSON);

        MvcResult resultado = mockMvc.perform(get)
                .andExpect(status().isOk())
                .andReturn();

        String json = resultado.getResponse().getContentAsString();
        List<RelatorioSomaSacasColhidas> relatorio = mapper.readValue(json, new TypeReference<>() {});

        Assertions.assertEquals(2, relatorio.size());

        Assertions.assertEquals("Fazenda 1", relatorio.get(0).getFazenda());
        Assertions.assertEquals(new BigDecimal("14.92"), relatorio.get(0).getSacasColhidas());

        Assertions.assertEquals("Fazenda 2", relatorio.get(1).getFazenda());
        Assertions.assertEquals(new BigDecimal("11.75"), relatorio.get(1).getSacasColhidas());
    }
}