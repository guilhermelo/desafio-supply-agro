package melo.rodrigues.guilherme.desafiosupplyagro.plantio.dominio;

import com.totvs.tjf.api.jpa.repository.ApiJpaRepository;
import melo.rodrigues.guilherme.desafiosupplyagro.plantio.api.EventoProjecao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

public interface PlantioRepository extends JpaRepository<Plantio, UUID>, ApiJpaRepository<Plantio> {
    @Query(value = "select p.id as id, p.data as data, p.talhao as talhao from Plantio p",
           countQuery = "select count(p) from Plantio p")
    Page<EventoProjecao> selecionaResumido(Pageable paginacao);

    @Query(value = "SELECT new melo.rodrigues.guilherme.desafiosupplyagro.plantio.dominio.RelatorioSomaAreaPlantada(f.nome, p.variedade, sum(p.areaPlantadaEmHectare)) " +
            " from Plantio p JOIN p.talhao t JOIN t.fazenda f where p.data between :dataInicial and :dataFinal " +
            "GROUP BY f.nome, p.variedade ORDER BY f.nome, p.variedade")
    List<RelatorioSomaAreaPlantada> relatorioSomaAreaPlantada(@Param("dataInicial") ZonedDateTime dataInicial, @Param("dataFinal") ZonedDateTime dataFinal);
}
