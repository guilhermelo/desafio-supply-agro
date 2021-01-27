package melo.rodrigues.guilherme.desafiosupplyagro.colheita.dominio;

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

public interface ColheitaRepository extends JpaRepository<Colheita, UUID>, ApiJpaRepository<Colheita> {

    @Query(value = "select c from Colheita c join fetch c.talhao",
           countQuery = "select count(c) from Colheita c")
    Page<Colheita> fetchAll(Pageable paginacao);

    @Query(value = "select c.id as id, c.data as data, c.talhao as talhao from Colheita c",
           countQuery = "select count(c) from Colheita c")
    Page<EventoProjecao> selecionaResumido(Pageable paginacao);

    @Query(value = "SELECT new melo.rodrigues.guilherme.desafiosupplyagro.colheita.dominio.RelatorioSomaSacasColhidas(f.nome, round((sum(c.pesoColhidoEmKg) / 60), 2)) " +
            "from Colheita c JOIN c.talhao t JOIN t.fazenda f where c.data between :dataInicial and :dataFinal GROUP BY f.nome ORDER BY f.nome")
    List<RelatorioSomaSacasColhidas> relatorioSomaSacasColhidas(@Param("dataInicial") ZonedDateTime dataInicial, @Param("dataFinal") ZonedDateTime dataFinal);
}
