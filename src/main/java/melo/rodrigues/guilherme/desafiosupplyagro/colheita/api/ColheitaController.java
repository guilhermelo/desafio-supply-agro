package melo.rodrigues.guilherme.desafiosupplyagro.colheita.api;

import com.totvs.tjf.api.context.stereotype.ApiGuideline;
import com.totvs.tjf.api.context.v2.request.ApiFieldRequest;
import com.totvs.tjf.api.context.v2.request.ApiPageRequest;
import com.totvs.tjf.api.context.v2.response.ApiCollectionResponse;
import com.totvs.tjf.api.jpa.ApiRequestConverter;
import com.totvs.tjf.core.api.jpa.repository.ApiJpaCollectionResult;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import melo.rodrigues.guilherme.desafiosupplyagro.colheita.dominio.Colheita;
import melo.rodrigues.guilherme.desafiosupplyagro.colheita.dominio.ColheitaRepository;
import melo.rodrigues.guilherme.desafiosupplyagro.colheita.dominio.RelatorioSomaSacasColhidas;
import melo.rodrigues.guilherme.desafiosupplyagro.colheita.infra.RelatorioColheitaRepository;
import melo.rodrigues.guilherme.desafiosupplyagro.plantio.api.EventoProjecao;
import melo.rodrigues.guilherme.desafiosupplyagro.plantio.api.Filtro;
import melo.rodrigues.guilherme.desafiosupplyagro.talhao.api.exceptions.TalhaoNaoEncontradoException;
import melo.rodrigues.guilherme.desafiosupplyagro.talhao.dominio.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@AllArgsConstructor
@ApiGuideline(ApiGuideline.ApiGuidelineVersion.V2)
@RestController
@RequestMapping("/api/v1/colheitas")
public class ColheitaController {

    private final TalhaoRepository talhaoRepository;
    private final EventoRepository eventoRepository;
    private final ColheitaRepository colheitaRepository;
    private final RelatorioColheitaRepository relatorioColheitaRepository;

    @PostMapping
    @ApiOperation("Realiza um apontamento de colheita")
    public ResponseEntity<ColheitaDTO> inserir(@RequestBody @Valid InclusaoColheitaRequest request) {

        Talhao talhao = talhaoRepository.findById(request.getIdTalhao())
                                .orElseThrow(() -> new TalhaoNaoEncontradoException(request.getIdTalhao().toString()));

        Colheita colheita = request.toModel(talhao);

        talhao.adicionarColheita(colheita);

        talhaoRepository.save(talhao);

        return ResponseEntity.ok(ColheitaDTO.from(colheita));
    }

    @GetMapping("/{id}")
    @ApiOperation("Busca colheita por id")
    public ResponseEntity<ColheitaDTO> buscarPorId(@PathVariable("id") String id) {
        return colheitaRepository.findById(UUID.fromString(id))
                                 .map(colheita -> ResponseEntity.ok(ColheitaDTO.from(colheita)))
                                 .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @ApiOperation("Busca todas as colheitas com paginação")
    public ApiCollectionResponse<ColheitaDTO> buscaTodosPaginados(ApiFieldRequest apiFieldRequest, ApiPageRequest pageRequest) {

        ApiJpaCollectionResult<Colheita> resultado = colheitaRepository.findAllProjected(apiFieldRequest, pageRequest);

        List<ColheitaDTO> colheitas = resultado.getItems().stream().map(ColheitaDTO::from).collect(Collectors.toList());

        return ApiCollectionResponse.of(colheitas, resultado.hasNext());
    }

    @GetMapping("/resumo")
    @ApiOperation("Busca colheitas de forma resumida")
    public ApiCollectionResponse<EventoProjecao> buscaComProjecaoPaginadas(ApiPageRequest pageRequest) {
        Pageable paginacao = ApiRequestConverter.convert(pageRequest);
        Page<EventoProjecao> resultado = eventoRepository.selecionaComProjecaoPorTipo(paginacao, TipoEvento.COLHEITA);

        return ApiCollectionResponse.of(resultado.toList(), resultado.hasNext());
    }

    @GetMapping("/relatorio/soma/sacasColhidas")
    @ApiOperation("Retorna uma visão da soma de sacas colhidas por fazenda")
    public ResponseEntity<List<RelatorioSomaSacasColhidas>> relatorio(Filtro filtro) {
        List<RelatorioSomaSacasColhidas> relatorio = relatorioColheitaRepository.buscaSomaSacasPorFazenda(filtro);

        return ResponseEntity.ok(relatorio);
    }

}
