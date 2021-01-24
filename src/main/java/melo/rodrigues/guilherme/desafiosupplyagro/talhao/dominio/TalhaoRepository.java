package melo.rodrigues.guilherme.desafiosupplyagro.talhao.dominio;

import com.totvs.tjf.api.jpa.repository.ApiJpaRepository;
import melo.rodrigues.guilherme.desafiosupplyagro.fazenda.dominio.Fazenda;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TalhaoRepository extends JpaRepository<Talhao, UUID>, ApiJpaRepository<Talhao> {

    boolean existsByCodigoAndFazenda(String codigo, Fazenda fazenda);

    List<Talhao> findBySafra(Integer safra);
}
