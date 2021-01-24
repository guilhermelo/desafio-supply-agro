package melo.rodrigues.guilherme.desafiosupplyagro.plantio.dominio;

import com.totvs.tjf.api.jpa.repository.ApiJpaRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PlantioRepository extends JpaRepository<Plantio, UUID>, ApiJpaRepository<Plantio> {

}
