package melo.rodrigues.guilherme.desafiosupplyagro.talhao.dominio;

import com.totvs.tjf.api.jpa.repository.ApiJpaRepository;
import melo.rodrigues.guilherme.desafiosupplyagro.plantio.api.EventoProjecao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Transactional(readOnly = true)
public interface EventoRepository extends JpaRepository<Evento, UUID>, ApiJpaRepository<Evento> {

    List<Evento> findByTalhao(Talhao talhao);

    @Query("select e.id as id, e.data as data, e.talhao.id as talhao from Evento e where e.tipo = :tipo")
    Page<EventoProjecao> selecionaComProjecaoPorTipo(Pageable paginacao, @Param("tipo") TipoEvento tipo);
}
