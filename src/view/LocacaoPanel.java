package view;

import controller.LocacaoController;
import model.Locacao;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Painel de abertura de locações e registro de devoluções.
 * Responsabilidade exclusiva: apresentação e captura de entrada do usuário.
 * Toda lógica de negócio é delegada ao LocacaoController.
 */
public class LocacaoPanel extends JPanel {

    private final LocacaoController controller;

    // ── Campos — Nova Locação ─────────────────────────────────────────────────
    private final JTextField txtCpfCliente  = UITheme.campo();
    private final JTextField txtPlacaVeiculo = UITheme.campo();

    // ── Campos — Devolução ────────────────────────────────────────────────────
    private final JTextField txtIdLocacao = UITheme.campo();
    private final JTextField txtKmFinal   = UITheme.campo();

    // ── Tabela ────────────────────────────────────────────────────────────────
    private final DefaultTableModel modeloTabela;
    private final JTable tabela;

    public LocacaoPanel(LocacaoController controller) {
        this.controller = controller;
        setBackground(UITheme.C_BG);
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        modeloTabela = new DefaultTableModel(
                new String[]{"ID", "Cliente", "Veículo", "Saída", "Devolução", "Status", "Valor Total"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabela = UITheme.tabela(modeloTabela);

        JPanel painelEsquerdo = new JPanel(new GridLayout(2, 1, 0, 10));
        painelEsquerdo.setBackground(UITheme.C_BG);
        painelEsquerdo.add(construirCardLocacao());
        painelEsquerdo.add(construirCardDevolucao());

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                painelEsquerdo, construirLista());
        split.setDividerLocation(360);
        split.setBackground(UITheme.C_BG);
        split.setBorder(null);
        add(split, BorderLayout.CENTER);

        recarregarTabela();
        registrarListenerTabela();
    }

    // ── Card: Nova Locação ────────────────────────────────────────────────────

    private JPanel construirCardLocacao() {
        JPanel card = UITheme.card("Nova Locação (Saída)");

        JPanel grid = UITheme.gridPanel();
        GridBagConstraints g = UITheme.gbc();
        UITheme.addLinha(grid, g, 0, "CPF do Cliente:",   txtCpfCliente);
        UITheme.addLinha(grid, g, 1, "Placa do Veículo:", txtPlacaVeiculo);

        JButton btnLocar  = UITheme.botao("Registrar Saída", UITheme.C_SUCCESS);
        JButton btnLimpar = UITheme.botao("Limpar",          UITheme.C_BORDER);

        btnLocar .addActionListener(e -> onAbrirLocacao());
        btnLimpar.addActionListener(e -> UITheme.limpar(txtCpfCliente, txtPlacaVeiculo));

        card.add(grid, BorderLayout.CENTER);
        card.add(UITheme.painelBotoes(btnLimpar, btnLocar), BorderLayout.SOUTH);
        return card;
    }

    // ── Card: Devolução ───────────────────────────────────────────────────────

    private JPanel construirCardDevolucao() {
        JPanel card = UITheme.card("Registrar Devolução");

        JPanel grid = UITheme.gridPanel();
        GridBagConstraints g = UITheme.gbc();
        UITheme.addLinha(grid, g, 0, "ID da Locação:", txtIdLocacao);
        UITheme.addLinha(grid, g, 1, "KM Final:",      txtKmFinal);

        JLabel dica = UITheme.label("  💡 Clique em uma locação ativa na lista para preencher o ID");
        dica.setBorder(BorderFactory.createEmptyBorder(4, 0, 0, 0));

        JButton btnDevolver = UITheme.botao("Registrar Devolução", UITheme.C_DANGER);
        JButton btnLimpar   = UITheme.botao("Limpar",              UITheme.C_BORDER);

        btnDevolver.addActionListener(e -> onEncerrarLocacao());
        btnLimpar  .addActionListener(e -> UITheme.limpar(txtIdLocacao, txtKmFinal));

        JPanel centro = new JPanel(new BorderLayout(0, 4));
        centro.setBackground(UITheme.C_CARD);
        centro.add(grid, BorderLayout.NORTH);
        centro.add(dica, BorderLayout.CENTER);

        card.add(centro, BorderLayout.CENTER);
        card.add(UITheme.painelBotoes(btnLimpar, btnDevolver), BorderLayout.SOUTH);
        return card;
    }

    // ── Lista / Consulta ──────────────────────────────────────────────────────

    private JPanel construirLista() {
        JPanel card = UITheme.card("Histórico de Locações");

        JButton btnAtualizar = UITheme.botao("Atualizar",   UITheme.C_ACCENT);
        JButton btnSoAtivas  = UITheme.botao("Só Ativas",   UITheme.C_SUCCESS);

        btnAtualizar.addActionListener(e -> recarregarTabela());
        btnSoAtivas .addActionListener(e -> carregarSomenteAtivas());

        card.add(UITheme.painelBusca(btnAtualizar, btnSoAtivas), BorderLayout.NORTH);
        card.add(UITheme.scrollPane(tabela), BorderLayout.CENTER);
        return card;
    }

    // ── Handlers de eventos ───────────────────────────────────────────────────

    private void onAbrirLocacao() {
        try {
            Locacao loc = controller.abrirLocacao(
                    txtCpfCliente.getText(), txtPlacaVeiculo.getText());
            UITheme.dialogo(this,
                    "✅ Locação #" + loc.getId() + " registrada!\n"
                    + "Cliente : " + loc.getCliente().getNome() + "\n"
                    + "Veículo : " + loc.getAutomovel().getPlaca() + "\n"
                    + "Saída   : " + loc.getDataLocacao());
            UITheme.limpar(txtCpfCliente, txtPlacaVeiculo);
            recarregarTabela();
        } catch (Exception ex) {
            UITheme.erro(this, ex.getMessage());
        }
    }

    private void onEncerrarLocacao() {
        try {
            Locacao loc = controller.encerrarLocacao(
                    txtIdLocacao.getText(), txtKmFinal.getText());
            UITheme.dialogo(this,
                    "✅ Devolução registrada!\n"
                    + "Locação #" + loc.getId() + "\n"
                    + "Veículo : " + loc.getAutomovel().getPlaca() + "\n"
                    + "Dias    : " + calcularDias(loc) + "\n"
                    + "Total   : R$ " + String.format("%.2f", loc.getValorTotal()));
            UITheme.limpar(txtIdLocacao, txtKmFinal);
            recarregarTabela();
        } catch (Exception ex) {
            UITheme.erro(this, ex.getMessage());
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private void recarregarTabela() {
        modeloTabela.setRowCount(0);
        for (Locacao l : controller.listarTodas())
            modeloTabela.addRow(toRow(l));
    }

    private void carregarSomenteAtivas() {
        modeloTabela.setRowCount(0);
        for (Locacao l : controller.listarAtivas())
            modeloTabela.addRow(toRow(l));
    }

    private Object[] toRow(Locacao l) {
        return new Object[]{
                l.getId(),
                l.getCliente().getNome(),
                l.getAutomovel().getPlaca(),
                l.getDataLocacao(),
                l.getDataDevolucao() != null ? l.getDataDevolucao() : "—",
                l.getStatus(),
                l.getValorTotal() > 0
                        ? String.format("R$ %.2f", l.getValorTotal()) : "—"
        };
    }

    private long calcularDias(Locacao loc) {
        if (loc.getDataDevolucao() == null) return 0;
        long d = java.time.temporal.ChronoUnit.DAYS.between(
                loc.getDataLocacao(), loc.getDataDevolucao());
        return d == 0 ? 1 : d;
    }

    /** Clique em locação ativa preenche o campo de ID para devolução. */
    private void registrarListenerTabela() {
        tabela.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = tabela.getSelectedRow();
                if (row < 0) return;
                Object status = modeloTabela.getValueAt(row, 5);
                if (Locacao.Status.ATIVA.toString().equals(String.valueOf(status))) {
                    txtIdLocacao.setText(String.valueOf(modeloTabela.getValueAt(row, 0)));
                }
            }
        });
    }
}
