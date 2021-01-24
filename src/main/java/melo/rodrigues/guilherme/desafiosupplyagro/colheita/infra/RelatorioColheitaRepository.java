package melo.rodrigues.guilherme.desafiosupplyagro.colheita.infra;

import melo.rodrigues.guilherme.desafiosupplyagro.colheita.dominio.RelatorioSomaSacasColhidas;
import melo.rodrigues.guilherme.desafiosupplyagro.plantio.api.Filtro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class RelatorioColheitaRepository {

    @Autowired
    private EntityManager entityManager;

    public List<RelatorioSomaSacasColhidas> buscaSomaSacasPorFazenda(Filtro filtro) {

        StringBuilder query = new StringBuilder(" SELECT new melo.rodrigues.guilherme.desafiosupplyagro.colheita.dominio.RelatorioSomaSacasColhidas(f.nome, round((sum(c.pesoColhidoEmKg) / 60), 2)) ");
        query.append(" from Colheita c JOIN c.talhao t JOIN t.fazenda f ");


        if(filtro.isDatasInformadas()) {
            query.append(" where c.data between :dataInicial and :dataFinal");
        }

        query.append(" GROUP BY f.nome ORDER BY f.nome ");

        TypedQuery<RelatorioSomaSacasColhidas> typedQuery = entityManager.createQuery(query.toString(), RelatorioSomaSacasColhidas.class);

        if(filtro.isDatasInformadas()) {
            typedQuery.setParameter("dataInicial", filtro.getDataInicial());
            typedQuery.setParameter("dataFinal", filtro.getDataFinal());
        }

        return typedQuery.getResultList();
    }
}
