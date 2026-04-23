package repository;

import model.Locacao;
import java.util.List;
import java.util.Optional;

/**
 * Contrato de acesso a dados para Locacao.
 */
public interface LocacaoRepository {
    void        salvar(Locacao locacao);
    void        excluir(long id);
    Optional<Locacao> buscarPorId(long id);
    List<Locacao>     listarTodas();
    List<Locacao>     listarAtivas();
}
