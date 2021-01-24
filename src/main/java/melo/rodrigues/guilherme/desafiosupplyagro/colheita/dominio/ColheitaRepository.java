package melo.rodrigues.guilherme.desafiosupplyagro.colheita.dominio;

import com.totvs.tjf.api.jpa.repository.ApiJpaRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ColheitaRepository extends JpaRepository<Colheita, UUID>, ApiJpaRepository<Colheita> {
}
