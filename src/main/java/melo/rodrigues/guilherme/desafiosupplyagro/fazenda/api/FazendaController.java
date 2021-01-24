package melo.rodrigues.guilherme.desafiosupplyagro.fazenda.api;

import com.totvs.tjf.api.context.stereotype.ApiGuideline;
import com.totvs.tjf.api.context.v2.request.ApiFieldRequest;
import com.totvs.tjf.api.context.v2.request.ApiPageRequest;
import com.totvs.tjf.api.context.v2.request.ApiSortRequest;
import com.totvs.tjf.api.context.v2.response.ApiCollectionResponse;
import com.totvs.tjf.core.api.jpa.repository.ApiJpaCollectionResult;
import lombok.AllArgsConstructor;
import melo.rodrigues.guilherme.desafiosupplyagro.fazenda.api.exceptions.FazendaNaoEncontradaException;
import melo.rodrigues.guilherme.desafiosupplyagro.fazenda.dominio.Fazenda;
import melo.rodrigues.guilherme.desafiosupplyagro.fazenda.dominio.FazendaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@AllArgsConstructor
@ApiGuideline(ApiGuideline.ApiGuidelineVersion.V2)
@RestController
@RequestMapping("/api/v1/fazendas")
public class FazendaController {

    private final FazendaRepository repository;

    @PostMapping
    public ResponseEntity<FazendaDTO> inserir(@RequestBody @Valid InclusaoFazendaRequest request) {
        Fazenda fazenda = request.toModel();

        Fazenda fazendaCriada = repository.save(fazenda);

        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest()
                                                                 .path("/")
                                                                 .path(fazendaCriada.getId().toString())
                                                                 .build().toUri())
                                                                 .body(FazendaDTO.from(fazendaCriada));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FazendaDTO> alterar(@PathVariable("id") String id, @Valid @RequestBody EdicaoFazendaRequest request) {

        Fazenda fazenda = repository.findById(UUID.fromString(id))
                                    .orElseThrow(() -> new FazendaNaoEncontradaException(id));

        Fazenda fazendaAtualizada = fazenda.atualizaNome(request.getNome());

        return ResponseEntity.ok(FazendaDTO.from(repository.save(fazendaAtualizada)));
    }

    @GetMapping
    public ApiCollectionResponse<FazendaDTO> buscaFazendas(ApiFieldRequest campos, ApiSortRequest ordenacao, ApiPageRequest paginacao) {

        ApiJpaCollectionResult<Fazenda> resultado = repository.findAllProjected(campos, paginacao, ordenacao);
        List<FazendaDTO> fazendas = resultado.getItems().stream().map(FazendaDTO::from).collect(Collectors.toList());

        return ApiCollectionResponse.of(fazendas, resultado.hasNext());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FazendaDTO> buscaPorId(@PathVariable("id") String id) {
        return repository.findById(UUID.fromString(id))
                         .map(f -> ResponseEntity.ok(FazendaDTO.from(f)))
                         .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirPorId(@PathVariable("id") String id) {
        Fazenda fazenda = repository.findById(UUID.fromString(id))
                                    .orElseThrow(() -> new FazendaNaoEncontradaException(id));

        repository.deleteById(fazenda.getId());

        return ResponseEntity.noContent().build();
    }
}
