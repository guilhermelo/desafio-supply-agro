package melo.rodrigues.guilherme.desafiosupplyagro.talhao.dominio;

import lombok.RequiredArgsConstructor;
import melo.rodrigues.guilherme.desafiosupplyagro.talhao.api.exceptions.OperacaoInvalidaException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EncerrarTalhoes {

    private final TalhaoRepository talhaoRepository;

    @Transactional(rollbackFor = OperacaoInvalidaException.class)
    public List<Talhao> executar(Integer safra) {

        List<Talhao> talhoes = talhaoRepository.findBySafra(safra);

        List<Talhao> talhoesProximaSafra = new ArrayList<>();

        for (Talhao talhao : talhoes) {
            talhao.encerrar();

            Talhao talhaoParaProximaSafra = talhao.geraTalhaoParaProximaSafra();

            talhoesProximaSafra.add(talhaoParaProximaSafra);
        }

        talhaoRepository.saveAll(talhoesProximaSafra);
        return talhoesProximaSafra;
    }
}
