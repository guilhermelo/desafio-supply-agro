package melo.rodrigues.guilherme.desafiosupplyagro.talhao.api;

import com.totvs.tjf.api.context.stereotype.ApiGuideline;
import com.totvs.tjf.api.context.v2.request.ApiFieldRequest;
import com.totvs.tjf.api.context.v2.request.ApiPageRequest;
import com.totvs.tjf.api.context.v2.request.ApiSortRequest;
import com.totvs.tjf.api.context.v2.response.ApiCollectionResponse;
import com.totvs.tjf.core.api.jpa.repository.ApiJpaCollectionResult;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import melo.rodrigues.guilherme.desafiosupplyagro.fazenda.api.exceptions.FazendaNaoEncontradaException;
import melo.rodrigues.guilherme.desafiosupplyagro.fazenda.dominio.Fazenda;
import melo.rodrigues.guilherme.desafiosupplyagro.fazenda.dominio.FazendaRepository;
import melo.rodrigues.guilherme.desafiosupplyagro.talhao.api.exceptions.OperacaoInvalidaException;
import melo.rodrigues.guilherme.desafiosupplyagro.talhao.api.exceptions.TalhaoComCodigoFazendaException;
import melo.rodrigues.guilherme.desafiosupplyagro.talhao.api.exceptions.TalhaoNaoEncontradoException;
import melo.rodrigues.guilherme.desafiosupplyagro.talhao.dominio.EncerrarTalhoes;
import melo.rodrigues.guilherme.desafiosupplyagro.talhao.dominio.EventoRepository;
import melo.rodrigues.guilherme.desafiosupplyagro.talhao.dominio.Talhao;
import melo.rodrigues.guilherme.desafiosupplyagro.talhao.dominio.TalhaoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@AllArgsConstructor
@ApiGuideline(ApiGuideline.ApiGuidelineVersion.V2)
@RestController
@RequestMapping("/api/v1/talhoes")
public class TalhaoController {

    private final FazendaRepository fazendaRepository;
    private final TalhaoRepository talhaoRepository;
    private final EventoRepository eventoRepository;
    private final EncerrarTalhoes encerrarTalhoes;

    @PostMapping
    @ApiOperation("Insere um novo talhão")
    public ResponseEntity<TalhaoDTO> inserir(@RequestBody @Valid InclusaoTalhaoRequest request) {

        Fazenda fazenda = fazendaRepository.findById(request.getIdFazenda())
                              .orElseThrow(() -> new FazendaNaoEncontradaException(request.getIdFazenda().toString()));

        Talhao talhao = request.toModel(fazenda);

        if(talhaoRepository.existsByCodigoAndFazenda(talhao.getCodigo(), fazenda)) {
            throw new TalhaoComCodigoFazendaException(talhao.getCodigo(), fazenda.getNome());
        }

        Talhao talhaoCriado = talhaoRepository.save(talhao);

        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest()
                             .path("/")
                             .path(talhaoCriado.getId().toString())
                             .build().toUri())
                             .body(TalhaoDTO.from(talhaoCriado));
    }

    @PutMapping("/{id}")
    @ApiOperation("Altera a área e a estimativa do talhão")
    public ResponseEntity<TalhaoDTO> alterar(@PathVariable String id, @RequestBody @Valid EdicaoTalhaoRequest request) {
        Talhao talhao = talhaoRepository.findById(UUID.fromString(id))
                                        .orElseThrow(() -> new TalhaoNaoEncontradoException(id));

        Talhao talhaoAlterado = talhao.atualizaAreaEstimativa(request.getAreaEmHectare(), request.getEstimativaProdutividade());

        talhaoRepository.save(talhaoAlterado);

        return ResponseEntity.ok(TalhaoDTO.from(talhaoAlterado));
    }

    @GetMapping
    @ApiOperation("Busca todos os talhões")
    public ApiCollectionResponse<TalhaoDTO> buscaTodos(ApiFieldRequest campos, ApiSortRequest ordenacao, ApiPageRequest paginacao) {
        ApiJpaCollectionResult<Talhao> resultado = talhaoRepository.findAllProjected(campos, paginacao, ordenacao);

        List<TalhaoDTO> talhoes = resultado.getItems().stream().map(TalhaoDTO::from).collect(Collectors.toList());

        return ApiCollectionResponse.of(talhoes, resultado.hasNext());
    }

    @GetMapping("/{id}")
    @ApiOperation("Busca talhão por id")
    public ResponseEntity<TalhaoDTO> buscaPorId(@PathVariable String id) {
        return talhaoRepository.findById(UUID.fromString(id))
                               .map(value -> ResponseEntity.ok(TalhaoDTO.from(value)))
                               .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Exclui um talhão por id")
    public ResponseEntity<Void> excluirPorId(@PathVariable("id") String id) {
        Talhao talhao = talhaoRepository.findById(UUID.fromString(id))
                                        .orElseThrow(() -> new TalhaoNaoEncontradoException(id));

        talhaoRepository.deleteById(talhao.getId());

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/eventos")
    @ApiOperation("Retorna os eventos do talhão")
    public ApiCollectionResponse<EventoDTO> recuperaTodosEventosDoTalhao(@PathVariable("id") String id) {
        Talhao talhao = talhaoRepository.findById(UUID.fromString(id))
                                        .orElseThrow(() -> new TalhaoNaoEncontradoException(id));

        List<EventoDTO> eventos = eventoRepository.findByTalhao(talhao).stream().map(EventoDTO::from)
                                                  .collect(Collectors.toList());

        return ApiCollectionResponse.from(eventos);
    }

    @PutMapping("/encerra/{safra}")
    @ApiOperation("Encerra um talhão e cria um novo para a próxima safra")
    @Transactional(rollbackFor = OperacaoInvalidaException.class)
    public ResponseEntity<List<TalhaoDTO>> encerraTalhao(@PathVariable("safra") Integer safra) {

        List<Talhao> talhoesProximaSafra = encerrarTalhoes.executar(safra);

        return ResponseEntity.ok(talhoesProximaSafra.stream().map(TalhaoDTO::from).collect(Collectors.toList()));
    }


}
