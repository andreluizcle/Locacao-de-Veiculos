package repository;

import model.Cliente;
import java.util.List;
import java.util.Optional;

/**
 * Contrato de acesso a dados para Cliente.
 * As Views e Controllers dependem desta interface, nunca da implementação concreta.
 */
public interface ClienteRepository {
    void        salvar(Cliente cliente);
    void        excluir(long cpf);
    Optional<Cliente> buscarPorCpf(long cpf);
    List<Cliente>     listarTodos();
}
