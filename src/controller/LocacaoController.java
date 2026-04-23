package controller;

import model.Automovel;
import model.Cliente;
import model.Locacao;
import repository.AutomovelRepository;
import repository.ClienteRepository;
import repository.LocacaoRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Coordena as operações de Locação e Devolução.
 *
 * Responsabilidades:
 *  - Resolver referências (buscar Cliente e Automovel pelos IDs fornecidos pela View).
 *  - Garantir regras de negócio: veículo disponível, locação ativa para devolução, etc.
 *  - Orquestrar a persistência: após encerrar uma locação, salvar tanto a locação
 *    quanto o automóvel atualizado.
 */
public class LocacaoController {

    private final LocacaoRepository   locacaoRepo;
    private final ClienteRepository   clienteRepo;
    private final AutomovelRepository automovelRepo;

    public LocacaoController(LocacaoRepository locacaoRepo,
                             ClienteRepository clienteRepo,
                             AutomovelRepository automovelRepo) {
        this.locacaoRepo   = locacaoRepo;
        this.clienteRepo   = clienteRepo;
        this.automovelRepo = automovelRepo;
    }

    /**
     * Abre uma nova locação para o cliente e veículo informados.
     *
     * @return a Locacao criada (com ID gerado).
     * @throws IllegalArgumentException se cliente ou veículo não existirem.
     * @throws IllegalStateException    se o veículo já possuir locação ativa.
     */
    public Locacao abrirLocacao(String cpfTexto, String placa) {
        long   cpf      = parseLong(cpfTexto, "CPF");
        String placaNorm = placa.trim().toUpperCase();

        Cliente cliente = clienteRepo.buscarPorCpf(cpf)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Cliente com CPF " + cpf + " não encontrado."));

        Automovel automovel = automovelRepo.buscarPorPlaca(placaNorm)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Veículo com placa " + placaNorm + " não encontrado."));

        boolean jaLocado = locacaoRepo.listarAtivas().stream()
                .anyMatch(l -> l.getAutomovel().getPlaca().equalsIgnoreCase(placaNorm));
        if (jaLocado)
            throw new IllegalStateException(
                    "Veículo " + placaNorm + " já possui uma locação ativa.");

        Locacao locacao = new Locacao(LocalDate.now(), LocalTime.now(), cliente, automovel);
        locacaoRepo.salvar(locacao);
        return locacao;
    }

    /**
     * Encerra uma locação ativa, registrando a devolução e calculando o valor.
     *
     * @return a Locacao encerrada com o valor total calculado.
     * @throws IllegalArgumentException se a locação não for encontrada.
     * @throws IllegalStateException    se a locação não estiver ativa.
     */
    public Locacao encerrarLocacao(String idTexto, String kmTexto) {
        long id = parseLong(idTexto, "ID da locação");
        long km = parseLong(kmTexto, "KM final");

        Locacao locacao = locacaoRepo.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Locação #" + id + " não encontrada."));

        // encerrar() lança IllegalStateException se não estiver ativa
        locacao.encerrar(LocalDate.now(), LocalTime.now(), km);

        // Persiste locação encerrada e automóvel com quilometragem atualizada
        locacaoRepo.salvar(locacao);
        automovelRepo.salvar(locacao.getAutomovel());

        return locacao;
    }

    /** Retorna todas as locações (ativas, finalizadas e canceladas). */
    public List<Locacao> listarTodas() {
        return locacaoRepo.listarTodas();
    }

    /** Retorna apenas as locações com status ATIVA. */
    public List<Locacao> listarAtivas() {
        return locacaoRepo.listarAtivas();
    }

    // ── Helper ────────────────────────────────────────────────────────────────

    private long parseLong(String texto, String campo) {
        if (texto == null || texto.isBlank())
            throw new IllegalArgumentException("Campo obrigatório: " + campo + ".");
        try {
            return Long.parseLong(texto.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(campo + " inválido — esperado número inteiro.");
        }
    }
}
