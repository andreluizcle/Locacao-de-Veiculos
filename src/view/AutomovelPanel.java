package view;

import controller.AutomovelController;
import model.Automovel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Painel de CRUD de automóveis.
 * Responsabilidade exclusiva: apresentação e captura de entrada do usuário.
 * Toda lógica de negócio é delegada ao AutomovelController.
 */
public class AutomovelPanel extends JPanel {

    private final AutomovelController controller;

    // ── Campos do formulário ──────────────────────────────────────────────────
    private final JTextField txtPlaca    = UITheme.campo();
    private final JTextField txtRenavam  = UITheme.campo();
    private final JTextField txtChassi   = UITheme.campo();
    private final JTextField txtCor      = UITheme.campo();
    private final JTextField txtPortas   = UITheme.campo();
    private final JTextField txtKm       = UITheme.campo();
    private final JTextField txtValor    = UITheme.campo();
    private final JTextField txtModelo   = UITheme.campo();
    private final JTextField txtMarca    = UITheme.campo();
    private final JComboBox<String> cbCombustivel = new JComboBox<>(new String[]{
            "1 - Gasolina", "2 - Etanol", "3 - Diesel", "4 - Flex", "5 - Elétrico"});

    // ── Tabela ────────────────────────────────────────────────────────────────
    private final DefaultTableModel modeloTabela;
    private final JTable tabela;

    // ── Busca ─────────────────────────────────────────────────────────────────
    private final JTextField txtBusca = UITheme.campo();

    public AutomovelPanel(AutomovelController controller) {
        this.controller = controller;
        setBackground(UITheme.C_BG);
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        modeloTabela = new DefaultTableModel(
                new String[]{"Placa", "Marca", "Modelo", "Cor", "KM", "Valor/Dia"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabela = UITheme.tabela(modeloTabela);
        UITheme.estilizarCombo(cbCombustivel);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                construirFormulario(), construirLista());
        split.setDividerLocation(390);
        split.setBackground(UITheme.C_BG);
        split.setBorder(null);
        add(split, BorderLayout.CENTER);

        recarregarTabela();
        registrarListenerTabela();
    }

    // ── Formulário ────────────────────────────────────────────────────────────

    private JPanel construirFormulario() {
        JPanel card = UITheme.card("Cadastrar / Editar Veículo");

        JPanel grid = UITheme.gridPanel();
        GridBagConstraints g = UITheme.gbc();
        UITheme.addLinha(grid, g, 0, "Placa:",           txtPlaca);
        UITheme.addLinha(grid, g, 1, "RENAVAM:",         txtRenavam);
        UITheme.addLinha(grid, g, 2, "Chassi:",          txtChassi);
        UITheme.addLinha(grid, g, 3, "Cor:",             txtCor);
        UITheme.addLinha(grid, g, 4, "Nº Portas:",       txtPortas);
        UITheme.addLinha(grid, g, 5, "Combustível:",     cbCombustivel);
        UITheme.addLinha(grid, g, 6, "KM atual:",        txtKm);
        UITheme.addLinha(grid, g, 7, "Valor/Diária R$:", txtValor);
        UITheme.addLinha(grid, g, 8, "Modelo:",          txtModelo);
        UITheme.addLinha(grid, g, 9, "Marca:",           txtMarca);

        JButton btnSalvar  = UITheme.botao("Salvar",  UITheme.C_ACCENT);
        JButton btnExcluir = UITheme.botao("Excluir", UITheme.C_DANGER);
        JButton btnLimpar  = UITheme.botao("Limpar",  UITheme.C_BORDER);

        btnSalvar .addActionListener(e -> onSalvar());
        btnExcluir.addActionListener(e -> onExcluir());
        btnLimpar .addActionListener(e -> limparFormulario());

        card.add(grid, BorderLayout.CENTER);
        card.add(UITheme.painelBotoes(btnLimpar, btnExcluir, btnSalvar), BorderLayout.SOUTH);
        return card;
    }

    // ── Lista / Consulta ──────────────────────────────────────────────────────

    private JPanel construirLista() {
        JPanel card = UITheme.card("Frota Cadastrada");

        txtBusca.setPreferredSize(new Dimension(160, 30));
        JButton btnBuscar = UITheme.botao("Buscar",       UITheme.C_ACCENT);
        JButton btnListar = UITheme.botao("Listar Todos", UITheme.C_SUCCESS);

        btnBuscar.addActionListener(e -> onBuscar());
        btnListar.addActionListener(e -> recarregarTabela());

        card.add(UITheme.painelBusca(
                UITheme.label("Placa:"), txtBusca, btnBuscar, btnListar),
                BorderLayout.NORTH);
        card.add(UITheme.scrollPane(tabela), BorderLayout.CENTER);
        return card;
    }

    // ── Handlers de eventos ───────────────────────────────────────────────────

    private void onSalvar() {
        try {
            int tipoCombustivel = cbCombustivel.getSelectedIndex() + 1;
            controller.salvar(
                    txtPlaca.getText(), txtRenavam.getText(), txtChassi.getText(),
                    txtCor.getText(),   txtPortas.getText(),  tipoCombustivel,
                    txtKm.getText(),    txtValor.getText(),
                    txtModelo.getText(), txtMarca.getText());
            UITheme.dialogo(this, "✅ Veículo salvo com sucesso!");
            limparFormulario();
            recarregarTabela();
        } catch (Exception ex) {
            UITheme.erro(this, ex.getMessage());
        }
    }

    private void onExcluir() {
        String placa = txtPlaca.getText().trim().toUpperCase();
        if (placa.isEmpty()) { UITheme.erro(this, "Informe a placa para excluir."); return; }
        if (!UITheme.confirmar(this, "Confirmar exclusão do veículo " + placa + "?")) return;
        try {
            controller.excluir(placa);
            UITheme.dialogo(this, "Veículo excluído.");
            limparFormulario();
            recarregarTabela();
        } catch (Exception ex) {
            UITheme.erro(this, ex.getMessage());
        }
    }

    private void onBuscar() {
        try {
            Automovel a = controller.buscar(txtBusca.getText());
            modeloTabela.setRowCount(0);
            modeloTabela.addRow(toRow(a));
        } catch (Exception ex) {
            UITheme.erro(this, ex.getMessage());
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private void recarregarTabela() {
        modeloTabela.setRowCount(0);
        for (Automovel a : controller.listarTodos())
            modeloTabela.addRow(toRow(a));
    }

    private Object[] toRow(Automovel a) {
        return new Object[]{
                a.getPlaca(),
                a.getModelo().getMarca().getDescricao(),
                a.getModelo().getDescricao(),
                a.getCor(),
                a.getQuilometragem(),
                String.format("R$ %.2f", a.getValorDiaria())
        };
    }

    private void limparFormulario() {
        UITheme.limpar(txtPlaca, txtRenavam, txtChassi, txtCor,
                txtPortas, txtKm, txtValor, txtModelo, txtMarca);
        cbCombustivel.setSelectedIndex(0);
    }

    /** Clique em linha preenche o formulário para edição. */
    private void registrarListenerTabela() {
        tabela.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = tabela.getSelectedRow();
                if (row < 0) return;
                String placa = String.valueOf(modeloTabela.getValueAt(row, 0));
                try {
                    Automovel a = controller.buscar(placa);
                    txtPlaca.setText(a.getPlaca());
                    txtRenavam.setText(String.valueOf(a.getRenavam()));
                    txtChassi.setText(a.getChassi());
                    txtCor.setText(a.getCor());
                    txtPortas.setText(String.valueOf(a.getNumeroPortas()));
                    txtKm.setText(String.valueOf(a.getQuilometragem()));
                    txtValor.setText(String.valueOf(a.getValorDiaria()));
                    txtModelo.setText(a.getModelo().getDescricao());
                    txtMarca.setText(a.getModelo().getMarca().getDescricao());
                    cbCombustivel.setSelectedIndex(a.getTipoCombustivel() - 1);
                } catch (Exception ex) {
                    UITheme.erro(AutomovelPanel.this, ex.getMessage());
                }
            }
        });
    }
}
