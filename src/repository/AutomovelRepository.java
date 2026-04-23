package repository;

import model.Automovel;
import java.util.List;
import java.util.Optional;

/**
 * Contrato de acesso a dados para Automovel.
 */
public interface AutomovelRepository {
    void        salvar(Automovel automovel);
    void        excluir(String placa);
    Optional<Automovel> buscarPorPlaca(String placa);
    List<Automovel>     listarTodos();
}
