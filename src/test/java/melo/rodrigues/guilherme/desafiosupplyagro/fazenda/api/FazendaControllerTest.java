package melo.rodrigues.guilherme.desafiosupplyagro.fazenda.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import melo.rodrigues.guilherme.desafiosupplyagro.ApplicationTests;
import melo.rodrigues.guilherme.desafiosupplyagro.fazenda.dominio.Endereco;
import melo.rodrigues.guilherme.desafiosupplyagro.fazenda.dominio.Fazenda;
import melo.rodrigues.guilherme.desafiosupplyagro.fazenda.dominio.FazendaRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = ApplicationTests.class)
class FazendaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private FazendaRepository repository;

    private static final String URI = "/api/v1/fazendas";
    private InclusaoFazendaRequest request;
    private Fazenda fazenda;

    @BeforeEach
    void setup() {
        request = new InclusaoFazendaRequest("Fazenda 1", "28.936.156/0001-06", "Assis", "SP", "Rua 1");
        Endereco endereco = Endereco.comCidadeEstadoLogradouro("Assis", "SP", "Rua 1");
        fazenda = Fazenda.comNomeCNPJEndereco("Fazenda 1", "28.936.156/0001-06", endereco);
    }

    @Test
    @DisplayName("Deve incluir fazenda")
    void deveIncluirFazenda() throws Exception {


        MockHttpServletRequestBuilder post = post(URI).locale(new Locale("pt", "BR"))
                                                      .contentType(MediaType.APPLICATION_JSON)
                                                      .content(mapper.writeValueAsString(request));

        mockMvc.perform(post).andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Não deve incluir fazenda quando informado nome e CNPJ de outra fazenda já cadastrada")
    void naoDeveIncluirFazendaComNomeECNPJExistentes() throws Exception {
        repository.save(fazenda);

        MockHttpServletRequestBuilder post = post(URI).locale(new Locale("pt", "BR"))
                                                      .contentType(MediaType.APPLICATION_JSON)
                                                      .content(mapper.writeValueAsString(request));

        mockMvc.perform(post)
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.details", Matchers.hasSize(2)));
    }

    @Test
    @DisplayName("Deve alterar o nome da fazenda")
    void deveAlterarNomeFazenda() throws Exception {
        repository.save(fazenda);

        EdicaoFazendaRequest edicaoRequest = new EdicaoFazendaRequest("Fazenda Assis");

        String uri = URI + "/" + fazenda.getId();
        MockHttpServletRequestBuilder put = put(uri).locale(new Locale("pt", "BR"))
                                                    .contentType(MediaType.APPLICATION_JSON)
                                                    .content(mapper.writeValueAsString(edicaoRequest));

        mockMvc.perform(put).andExpect(status().isOk());
        Optional<Fazenda> fazendaBuscada = repository.findById(fazenda.getId());
        fazendaBuscada.ifPresent((fazenda) -> Assertions.assertEquals(edicaoRequest.getNome(), fazenda.getNome()));
    }

    @Test
    @DisplayName("Deve excluir a fazenda de acordo com ID")
    void deveExcluirFazendaPorId() throws Exception {
        repository.save(fazenda);

        String uri = URI + "/" + fazenda.getId();
        MockHttpServletRequestBuilder delete = delete(uri).locale(new Locale("pt", "BR"))
                                                          .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(delete).andExpect(status().isNoContent());

        Optional<Fazenda> fazendaBuscada = repository.findById(fazenda.getId());
        Assertions.assertTrue(fazendaBuscada.isEmpty());
    }

    @Test
    @DisplayName("Não deve excluir a fazenda quando ID é inválido")
    void naoDeveExcluirFazendaComIdInvalido() throws Exception {

        String id = UUID.randomUUID().toString();
        String uri = URI + "/" + id;
        MockHttpServletRequestBuilder delete = delete(uri).locale(new Locale("pt", "BR"))
                                                          .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(delete).andExpect(status().isBadRequest())
                               .andExpect(jsonPath("$.message").value("Fazenda nao encontrada com id " + id));
    }

    @Test
    @DisplayName("Deve buscar fazenda de acordo com ID")
    void deveBuscarFazendaComId() throws Exception {

        repository.save(fazenda);

        String uri = URI + "/" + fazenda.getId();
        MockHttpServletRequestBuilder get = get(uri).locale(new Locale("pt", "BR"))
                                                    .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(get).andExpect(status().isOk());
    }

    @Test
    @DisplayName("Deve buscar apenas uma fazenda")
    void deveBuscarUmaFazendaPaginada() throws Exception {

        Endereco endereco = Endereco.comCidadeEstadoLogradouro("Assis", "SP", "Rua 1");
        Fazenda fazendaUm = Fazenda.comNomeCNPJEndereco("Fazenda 1", "69.096.927/0001-73", endereco);

        Endereco enderecoDois = Endereco.comCidadeEstadoLogradouro("Assis", "SP", "Rua 2");
        Fazenda fazendaDois = Fazenda.comNomeCNPJEndereco("Fazenda 2", "83.694.908/0001-70", enderecoDois);

        repository.saveAll(Arrays.asList(fazendaUm, fazendaDois));

        MockHttpServletRequestBuilder get = get(URI).locale(new Locale("pt", "BR"))
                                                    .param("pageSize", "1")
                                                    .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(get).andExpect(status().isOk())
                            .andExpect(jsonPath("$.items", Matchers.hasSize(1)));
    }
}