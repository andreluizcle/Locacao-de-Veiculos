import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class Locacao {
    private static long contadorId = 1;

    private long idLocacao;
    private LocalDate dataLocacao;
    private LocalTime horaLocacao;
    private LocalDate dataDevolucao;
    private LocalTime horaDevolucao;
    private long quilometragemFinal;
    private double valorTotal;
    private StatusLocacao status;

    private Cliente cliente;
    private Automovel automovel;

    public enum StatusLocacao {
        ATIVA, FINALIZADA, CANCELADA
    }

    public Locacao(LocalDate dataLocacao, LocalTime horaLocacao, Cliente cliente, Automovel automovel) {
        this.idLocacao = contadorId++;
        this.dataLocacao = dataLocacao;
        this.horaLocacao = horaLocacao;
        this.cliente = cliente;
        this.automovel = automovel;
        this.status = StatusLocacao.ATIVA;
    }

    // Construtor completo para leitura de arquivo
    public Locacao(long idLocacao, LocalDate dataLocacao, LocalTime horaLocacao,
                   LocalDate dataDevolucao, LocalTime horaDevolucao,
                   long quilometragemFinal, double valorTotal, StatusLocacao status,
                   Cliente cliente, Automovel automovel) {
        this.idLocacao = idLocacao;
        this.dataLocacao = dataLocacao;
        this.horaLocacao = horaLocacao;
        this.dataDevolucao = dataDevolucao;
        this.horaDevolucao = horaDevolucao;
        this.quilometragemFinal = quilometragemFinal;
        this.valorTotal = valorTotal;
        this.status = status;
        this.cliente = cliente;
        this.automovel = automovel;
        if (idLocacao >= contadorId) contadorId = idLocacao + 1;
    }

    /**
     * Registra a saída do veículo: marca o automóvel como locado
     * e persiste a locação no repositório.
     */
    public void registrarLocacao() {
        if (this.automovel == null || this.cliente == null) {
            throw new IllegalStateException("Locação inválida: cliente ou automóvel não informado.");
        }
        if (this.status != StatusLocacao.ATIVA) {
            throw new IllegalStateException("Esta locação já foi processada.");
        }
        // Salva a quilometragem de saída no próprio campo do automóvel (odômetro de saída)
        // e persiste
        RepositorioPersistencia.salvarLocacao(this);
        System.out.println("[Locacao] Locação #" + idLocacao + " registrada para "
                + cliente.getNomeCliente() + " - veículo " + automovel.getPlacaAutomovel());
    }

    /**
     * Registra a devolução do veículo: atualiza quilometragem, calcula valor total
     * e muda o status para FINALIZADA.
     */
    public void registrarDevolucao(LocalDate dataDevolucao, LocalTime horaDevolucao, long quilometragemFinal) {
        if (this.status != StatusLocacao.ATIVA) {
            throw new IllegalStateException("Esta locação não está ativa.");
        }

        this.dataDevolucao = dataDevolucao;
        this.horaDevolucao = horaDevolucao;
        this.quilometragemFinal = quilometragemFinal;
        this.status = StatusLocacao.FINALIZADA;

        // Atualiza o odômetro do automóvel
        this.automovel.setQuilometragemAutomovel(quilometragemFinal);

        // Calcula o valor: diárias * valor diário do veículo
        long diasLocados = ChronoUnit.DAYS.between(dataLocacao, dataDevolucao);
        if (diasLocados == 0) diasLocados = 1; // mínimo 1 diária
        this.valorTotal = diasLocados * automovel.getValorLocacaoAutomovel();

        // Persiste as alterações
        RepositorioPersistencia.atualizarLocacao(this);
        RepositorioPersistencia.atualizarAutomovel(this.automovel);

        System.out.println("[Locacao] Devolução registrada. Dias: " + diasLocados
                + " | Valor total: R$ " + valorTotal);
    }

    // ── Getters ──────────────────────────────────────────────────────────────

    public long getIdLocacao() { return idLocacao; }
    public LocalDate getDataLocacao() { return dataLocacao; }
    public LocalTime getHoraLocacao() { return horaLocacao; }
    public LocalDate getDataDevolucao() { return dataDevolucao; }
    public LocalTime getHoraDevolucao() { return horaDevolucao; }
    public long getQuilometragemFinal() { return quilometragemFinal; }
    public double getValorTotal() { return valorTotal; }
    public StatusLocacao getStatus() { return status; }
    public Cliente getCliente() { return cliente; }
    public Automovel getAutomovel() { return automovel; }

    // ── Setters necessários para atualização ─────────────────────────────────

    public void setStatus(StatusLocacao status) { this.status = status; }
    public void setValorTotal(double valorTotal) { this.valorTotal = valorTotal; }

    @Override
    public String toString() {
        return "Locacao{id=" + idLocacao
                + ", cliente=" + (cliente != null ? cliente.getNomeCliente() : "N/A")
                + ", veiculo=" + (automovel != null ? automovel.getPlacaAutomovel() : "N/A")
                + ", data=" + dataLocacao
                + ", status=" + status
                + ", valorTotal=" + valorTotal + "}";
    }
}