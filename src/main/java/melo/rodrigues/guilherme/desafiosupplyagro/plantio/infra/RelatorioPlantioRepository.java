package melo.rodrigues.guilherme.desafiosupplyagro.plantio.infra;

import melo.rodrigues.guilherme.desafiosupplyagro.plantio.api.Filtro;
import melo.rodrigues.guilherme.desafiosupplyagro.plantio.dominio.RelatorioSomaAreaPlantada;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class RelatorioPlantioRepository {

    @Autowired
    private EntityManager entityManager;

    public List<RelatorioSomaAreaPlantada> buscaSomaAreaPlantadaPorFazendaVariedade(Filtro filtro) {

        StringBuilder query = new StringBuilder(" SELECT new melo.rodrigues.guilherme.desafiosupplyagro.plantio.dominio.RelatorioSomaAreaPlantada(f.nome, p.variedade, sum(p.areaPlantadaEmHectare)) ");
        query.append(" from Plantio p JOIN p.talhao t JOIN t.fazenda f ");

        if(filtro.isDatasInformadas()) {
            query.append(" where p.data between :dataInicial and :dataFinal");
        }

        query.append(" GROUP BY f.nome, p.variedade ORDER BY f.nome, p.variedade ");

        TypedQuery<RelatorioSomaAreaPlantada> typedQuery = entityManager.createQuery(query.toString(), RelatorioSomaAreaPlantada.class);

        if(filtro.isDatasInformadas()) {
            typedQuery.setParameter("dataInicial", filtro.getDataInicial());
            typedQuery.setParameter("dataFinal", filtro.getDataFinal());
        }

        return typedQuery.getResultList();
    }
}
