package controller;

import model.Automovel;
import model.Marca;
import model.Modelo;
import repository.AutomovelRepository;
import repository.LocacaoRepository;

import java.util.List;

/**
 * Coordena as operações relacionadas a Automovel.
 *
 * Responsabilidades:
 *  - Montar o objeto Automovel a partir de strings vindas da View.
 *  - Validar dados de entrada.
 *  - Impedir exclusão de veículo com locação ativa.
 */
public class AutomovelController {

    private final AutomovelRepository repositorio;
    private final LocacaoRepository   locacaoRepo;

    public AutomovelController(AutomovelRepository repositorio, LocacaoRepository locacaoRepo) {
        this.repositorio = repositorio;
        this.locacaoRepo = locacaoRepo;
    }

    /**
     * Cadastra ou atualiza um automóvel (upsert por placa).
     */
    public void salvar(String placa, String renavamTxt, String chassi, String cor,
                       String portasTxt, int tipoCombustivel,
                       String kmTxt, String valorTxt,
                       String descModelo, String descMarca) {

        validarCampoObrigatorio(placa,      "Placa");
        validarCampoObrigatorio(descModelo, "Modelo");
        validarCampoObrigatorio(descMarca,  "Marca");

        long   renavam = parseLong(renavamTxt, "RENAVAM");
        int    portas  = parseInt(portasTxt,   "Nº Portas");
        long   km      = parseLong(kmTxt,      "KM atual");
        double valor   = parseDouble(valorTxt, "Valor da diária");

        Marca  marca  = new Marca(descMarca.trim());
        Modelo modelo = new Modelo(descModelo.trim(), marca);

        Automovel auto = new Automovel(placa.trim().toUpperCase(), renavam, chassi.trim(),
                cor.trim(), portas, tipoCombustivel, km, valor, modelo);
        repositorio.salvar(auto);
    }

    /**
     * Remove um automóvel pela placa.
     *
     * @throws IllegalStateException se houver locação ativa para este veículo.
     */
    public void excluir(String placa) {
        validarCampoObrigatorio(placa, "Placa");
        String placaNorm = placa.trim().toUpperCase();

        if (repositorio.buscarPorPlaca(placaNorm).isEmpty())
            throw new IllegalArgumentException("Veículo com placa " + placaNorm + " não encontrado.");

        boolean temLocacaoAtiva = locacaoRepo.listarAtivas().stream()
                .anyMatch(l -> l.getAutomovel().getPlaca().equalsIgnoreCase(placaNorm));
        if (temLocacaoAtiva)
            throw new IllegalStateException(
                    "Não é possível excluir: veículo possui locação ativa.");

        repositorio.excluir(placaNorm);
    }

    /**
     * Busca um automóvel pela placa.
     *
     * @throws IllegalArgumentException se não encontrado.
     */
    public Automovel buscar(String placa) {
        validarCampoObrigatorio(placa, "Placa");
        return repositorio.buscarPorPlaca(placa.trim().toUpperCase())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Nenhum veículo encontrado com a placa " + placa.trim().toUpperCase() + "."));
    }

    /** Retorna todos os automóveis da frota. */
    public List<Automovel> listarTodos() {
        return repositorio.listarTodos();
    }

    // ── Helpers de validação e conversão ─────────────────────────────────────

    private void validarCampoObrigatorio(String valor, String nomeCampo) {
        if (valor == null || valor.isBlank())
            throw new IllegalArgumentException("Campo obrigatório: " + nomeCampo + ".");
    }

    private long parseLong(String texto, String campo) {
        try { return Long.parseLong(texto.trim()); }
        catch (NumberFormatException e) {
            throw new IllegalArgumentException("Valor inválido para " + campo + " — esperado número inteiro.");
        }
    }

    private int parseInt(String texto, String campo) {
        try { return Integer.parseInt(texto.trim()); }
        catch (NumberFormatException e) {
            throw new IllegalArgumentException("Valor inválido para " + campo + " — esperado número inteiro.");
        }
    }

    private double parseDouble(String texto, String campo) {
        try { return Double.parseDouble(texto.trim().replace(",", ".")); }
        catch (NumberFormatException e) {
            throw new IllegalArgumentException("Valor inválido para " + campo + " — esperado número decimal.");
        }
    }
}
