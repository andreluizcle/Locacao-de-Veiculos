package controller;

import model.Cliente;
import repository.ClienteRepository;

import java.util.List;
import java.util.Optional;

/**
 * Coordena as operações relacionadas a Cliente.
 *
 * Responsabilidades:
 *  - Validar dados de entrada antes de delegar ao repositório.
 *  - Centralizar regras de negócio (ex.: CPF único, campos obrigatórios).
 *  - Isolar a View de qualquer conhecimento sobre persistência.
 *
 * A View chama métodos deste controller e recebe resultados simples
 * (objetos de modelo ou exceções com mensagens amigáveis).
 */
public class ClienteController {

    private final ClienteRepository repositorio;

    public ClienteController(ClienteRepository repositorio) {
        this.repositorio = repositorio;
    }

    /**
     * Cadastra um novo cliente ou atualiza um existente (upsert por CPF).
     *
     * @throws IllegalArgumentException se algum campo obrigatório estiver vazio ou CPF inválido.
     */
    public void salvar(String cpfTexto, String nome, String endereco,
                       String telefone, String email) {
        long cpf = parseCpf(cpfTexto);
        validarCampoObrigatorio(nome,     "Nome");
        validarCampoObrigatorio(endereco, "Endereço");
        validarCampoObrigatorio(telefone, "Telefone");
        validarCampoObrigatorio(email,    "E-mail");

        Cliente cliente = new Cliente(cpf, nome.trim(), endereco.trim(),
                telefone.trim(), email.trim());
        repositorio.salvar(cliente);
    }

    /**
     * Remove um cliente pelo CPF.
     *
     * @throws IllegalArgumentException se o CPF for inválido ou o cliente não existir.
     */
    public void excluir(String cpfTexto) {
        long cpf = parseCpf(cpfTexto);
        if (repositorio.buscarPorCpf(cpf).isEmpty())
            throw new IllegalArgumentException("Cliente com CPF " + cpf + " não encontrado.");
        repositorio.excluir(cpf);
    }

    /**
     * Busca um cliente pelo CPF.
     *
     * @throws IllegalArgumentException se não encontrado.
     */
    public Cliente buscar(String cpfTexto) {
        long cpf = parseCpf(cpfTexto);
        return repositorio.buscarPorCpf(cpf)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Nenhum cliente encontrado com CPF " + cpf + "."));
    }

    /** Retorna todos os clientes cadastrados. */
    public List<Cliente> listarTodos() {
        return repositorio.listarTodos();
    }

    // ── Helpers de validação ──────────────────────────────────────────────────

    private long parseCpf(String texto) {
        if (texto == null || texto.isBlank())
            throw new IllegalArgumentException("CPF não pode ser vazio.");
        try {
            long cpf = Long.parseLong(texto.trim());
            if (cpf <= 0) throw new NumberFormatException();
            return cpf;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("CPF inválido — digite apenas números.");
        }
    }

    private void validarCampoObrigatorio(String valor, String nomeCampo) {
        if (valor == null || valor.isBlank())
            throw new IllegalArgumentException("Campo obrigatório: " + nomeCampo + ".");
    }
}
