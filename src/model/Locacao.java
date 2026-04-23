package model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

/**
 * Representa uma locação de veículo.
 *
 * Esta classe contém apenas dados e regras de domínio.
 * Ela não sabe nada sobre persistência nem sobre a interface gráfica —
 * ambas as responsabilidades ficam no repositório e no controller, respectivamente.
 */
public class Locacao {

    // Contador estático de IDs; o repositório pode sincronizá-lo ao carregar dados.
    private static long proximoId = 1;

    public enum Status { ATIVA, FINALIZADA, CANCELADA }

    private long      id;
    private LocalDate dataLocacao;
    private LocalTime horaLocacao;
    private LocalDate dataDevolucao;
    private LocalTime horaDevolucao;
    private long      quilometragemFinal;
    private double    valorTotal;
    private Status    status;
    private Cliente   cliente;
    private Automovel automovel;

    // ── Construtor para nova locação ──────────────────────────────────────────

    public Locacao(LocalDate dataLocacao, LocalTime horaLocacao,
                   Cliente cliente, Automovel automovel) {
        this.id          = proximoId++;
        this.dataLocacao = dataLocacao;
        this.horaLocacao = horaLocacao;
        this.cliente     = cliente;
        this.automovel   = automovel;
        this.status      = Status.ATIVA;
    }

    // ── Construtor de reconstrução (usado pelo repositório) ───────────────────

    public Locacao(long id, LocalDate dataLocacao, LocalTime horaLocacao,
                   LocalDate dataDevolucao, LocalTime horaDevolucao,
                   long quilometragemFinal, double valorTotal, Status status,
                   Cliente cliente, Automovel automovel) {
        this.id                 = id;
        this.dataLocacao        = dataLocacao;
        this.horaLocacao        = horaLocacao;
        this.dataDevolucao      = dataDevolucao;
        this.horaDevolucao      = horaDevolucao;
        this.quilometragemFinal = quilometragemFinal;
        this.valorTotal         = valorTotal;
        this.status             = status;
        this.cliente            = cliente;
        this.automovel          = automovel;
        sincronizarContador(id);
    }

    // ── Regras de domínio ─────────────────────────────────────────────────────

    /**
     * Calcula e registra a devolução, atualizando quilometragem e valor total.
     * Não persiste — isso é responsabilidade do controller.
     */
    public void encerrar(LocalDate dataDev, LocalTime horaDev, long kmFinal) {
        if (status != Status.ATIVA)
            throw new IllegalStateException("Somente locações ativas podem ser encerradas.");

        this.dataDevolucao      = dataDev;
        this.horaDevolucao      = horaDev;
        this.quilometragemFinal = kmFinal;
        this.status             = Status.FINALIZADA;

        long dias = ChronoUnit.DAYS.between(dataLocacao, dataDev);
        if (dias == 0) dias = 1; // mínimo de 1 diária
        this.valorTotal = dias * automovel.getValorDiaria();

        // Atualiza o odômetro do automóvel (regra de negócio de domínio)
        automovel.setQuilometragem(kmFinal);
    }

    public void cancelar() {
        if (status != Status.ATIVA)
            throw new IllegalStateException("Somente locações ativas podem ser canceladas.");
        this.status = Status.CANCELADA;
    }

    public boolean isAtiva() { return status == Status.ATIVA; }

    // ── Sincronização do contador de IDs ─────────────────────────────────────

    public static void sincronizarContador(long idCarregado) {
        if (idCarregado >= proximoId) proximoId = idCarregado + 1;
    }

    // ── Getters ──────────────────────────────────────────────────────────────

    public long      getId()                  { return id; }
    public LocalDate getDataLocacao()         { return dataLocacao; }
    public LocalTime getHoraLocacao()         { return horaLocacao; }
    public LocalDate getDataDevolucao()       { return dataDevolucao; }
    public LocalTime getHoraDevolucao()       { return horaDevolucao; }
    public long      getQuilometragemFinal()  { return quilometragemFinal; }
    public double    getValorTotal()          { return valorTotal; }
    public Status    getStatus()              { return status; }
    public Cliente   getCliente()             { return cliente; }
    public Automovel getAutomovel()           { return automovel; }

    @Override
    public String toString() {
        return "Locacao{id=" + id
                + ", cliente=" + cliente.getNome()
                + ", veiculo=" + automovel.getPlaca()
                + ", status=" + status + "}";
    }
}
