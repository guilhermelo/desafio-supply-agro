package melo.rodrigues.guilherme.desafiosupplyagro.plantio.api;

import com.totvs.tjf.api.context.stereotype.ApiGuideline;
import com.totvs.tjf.api.context.v2.request.ApiPageRequest;
import com.totvs.tjf.api.context.v2.response.ApiCollectionResponse;
import com.totvs.tjf.api.jpa.ApiRequestConverter;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import melo.rodrigues.guilherme.desafiosupplyagro.plantio.dominio.Plantio;
import melo.rodrigues.guilherme.desafiosupplyagro.plantio.dominio.PlantioRepository;
import melo.rodrigues.guilherme.desafiosupplyagro.plantio.dominio.RelatorioSomaAreaPlantada;
import melo.rodrigues.guilherme.desafiosupplyagro.talhao.api.exceptions.TalhaoNaoEncontradoException;
import melo.rodrigues.guilherme.desafiosupplyagro.talhao.dominio.Talhao;
import melo.rodrigues.guilherme.desafiosupplyagro.talhao.dominio.TalhaoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@ApiGuideline(ApiGuideline.ApiGuidelineVersion.V2)
@RestController
@RequestMapping("/api/v1/plantios")
public class PlantioController {

    private final TalhaoRepository talhaoRepository;
    private final PlantioRepository plantioRepository;

    @PostMapping
    @ApiOperation("Realiza um novo apontamento de plantio")
    public ResponseEntity<PlantioDTO> inserir(@RequestBody @Valid InclusaoPlantioRequest request) {

        Talhao talhao = talhaoRepository.findById(request.getIdTalhao())
                                 .orElseThrow(() -> new TalhaoNaoEncontradoException(request.getIdTalhao().toString()));

        Plantio plantio = request.toModel(talhao);

        talhao.adicionarPlantio(plantio);

        talhaoRepository.save(talhao);

        return ResponseEntity.ok(PlantioDTO.from(plantio));
    }

    @GetMapping
    @ApiOperation("Busca todos os apontamentos de plantios")
    public ApiCollectionResponse<EventoProjecao> buscarTodos(ApiPageRequest pageRequest) {
        Pageable paginacao = ApiRequestConverter.convert(pageRequest);
        Page<EventoProjecao> resultado = plantioRepository.selecionaResumido(paginacao);

        return ApiCollectionResponse.of(resultado.toList(), resultado.hasNext());
    }

    @GetMapping("/{id}")
    @ApiOperation("Busca plantio por id")
    public ResponseEntity<PlantioDTO> buscarPorId(@PathVariable String id) {
        return plantioRepository.findById(UUID.fromString(id))
                               .map(plantio -> ResponseEntity.ok(PlantioDTO.from(plantio)))
                               .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/relatorio/soma/area")
    @ApiOperation("Retorna uma visão da soma da área plantada por fazenda e variedade")
    public ResponseEntity<List<RelatorioSomaAreaPlantada>> relatorio(Filtro filtro) {
        List<RelatorioSomaAreaPlantada> relatorio = plantioRepository.relatorioSomaAreaPlantada(filtro.getDataInicial(), filtro.getDataFinal());

        return ResponseEntity.ok(relatorio);
    }
}
