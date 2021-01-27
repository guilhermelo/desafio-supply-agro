package melo.rodrigues.guilherme.desafiosupplyagro.plantio.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import melo.rodrigues.guilherme.desafiosupplyagro.ApplicationTests;
import melo.rodrigues.guilherme.desafiosupplyagro.plantio.dominio.RelatorioSomaAreaPlantada;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = ApplicationTests.class)
@Sql(value = "classpath:scripts/insere_fazenda_talhao_evento.sql")
class PlantioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    private static final String API = "/api/v1/plantios";

    @Test
    @DisplayName("Busca todos os eventos de plantio")
    void deveBuscarTodosEventosPlantio() throws Exception {

        MockHttpServletRequestBuilder request = get(API).locale(new Locale("pt", "BR"))
                                                        .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
               .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("Busca plantio por ID")
    void deveBuscarPlantioPorID() throws Exception {

        final String id = "54de9035-05e3-4c0b-810f-1a6e5662f14d";

        final String uri = API + "/" + id;
        MockHttpServletRequestBuilder request = get(uri).locale(new Locale("pt", "BR"))
                                                        .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
               .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("Deve realizar o apontamento de plantio")
    void deveInserirPlantio() throws Exception {
        ZonedDateTime hoje = ZonedDateTime.now();
        final String idTalhao = "e5b3d4f6-023f-4bf5-ad54-9e6449138aed";
        InclusaoPlantioRequest request = new InclusaoPlantioRequest(UUID.fromString(idTalhao), new BigDecimal(50), "SOJA", hoje);

        MockHttpServletRequestBuilder post = post(API).locale(new Locale("pt", "BR"))
                                                      .contentType(MediaType.APPLICATION_JSON)
                                                      .content(mapper.writeValueAsString(request));
        mockMvc.perform(post)
               .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("Deve retornar um relatório da soma da área plantada por fazenda e variedade")
    void deveRetornarRelatorioSomaSacasColhidasComFiltroPorData() throws Exception {

        LocalDateTime localDateTime = LocalDateTime.of(2020, Month.JUNE, 19, 8, 30, 0);

        ZonedDateTime dataInicial = ZonedDateTime.of(localDateTime, ZoneId.systemDefault());
        ZonedDateTime dataFinal = dataInicial.plusDays(10);

        MultiValueMap<String, String> parametros = new LinkedMultiValueMap<>();
        parametros.add("dataInicial", dataInicial.toString());
        parametros.add("dataFinal", dataFinal.toString());

        MockHttpServletRequestBuilder get = get(API + "/relatorio/soma/area").locale(new Locale("pt", "BR"))
                                                                             .params(parametros)
                                                                             .contentType(MediaType.APPLICATION_JSON);

        MvcResult resultado = mockMvc.perform(get)
                                     .andExpect(status().isOk())
                                     .andReturn();

        String json = resultado.getResponse().getContentAsString();
        List<RelatorioSomaAreaPlantada> relatorio = mapper.readValue(json, new TypeReference<>() {});

        Assertions.assertEquals(2, relatorio.size());

        Assertions.assertEquals("Fazenda 1", relatorio.get(0).getFazenda());
        Assertions.assertEquals("Milho", relatorio.get(0).getVariedade());
        Assertions.assertEquals(new BigDecimal("195.00"), relatorio.get(0).getSomaAreaPlantada());

        Assertions.assertEquals("Fazenda 1", relatorio.get(1).getFazenda());
        Assertions.assertEquals("Soja", relatorio.get(1).getVariedade());
        Assertions.assertEquals(new BigDecimal("95.00"), relatorio.get(1).getSomaAreaPlantada());
    }
}