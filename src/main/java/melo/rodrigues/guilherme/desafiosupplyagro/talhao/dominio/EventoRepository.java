package melo.rodrigues.guilherme.desafiosupplyagro.talhao.dominio;

import com.totvs.tjf.api.jpa.repository.ApiJpaRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Transactional(readOnly = true)
public interface EventoRepository extends JpaRepository<Evento, UUID>, ApiJpaRepository<Evento> {

    List<Evento> findByTalhao(Talhao talhao);
}
