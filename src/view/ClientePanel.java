package view;

import controller.ClienteController;
import model.Cliente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * Painel de CRUD de clientes.
 *
 * Esta classe só lida com apresentação:
 *  - Monta componentes Swing.
 *  - Lê valores dos campos e os passa ao controller como Strings.
 *  - Recebe de volta objetos de modelo ou exceções e os exibe ao usuário.
 *
 * Não contém nenhuma regra de negócio nem acessa repositórios diretamente.
 */
public class ClientePanel extends JPanel {

    private final ClienteController controller;

    // ── Campos do formulário ──────────────────────────────────────────────────
    private final JTextField txtCpf      = UITheme.campo();
    private final JTextField txtNome     = UITheme.campo();
    private final JTextField txtEndereco = UITheme.campo();
    private final JTextField txtTelefone = UITheme.campo();
    private final JTextField txtEmail    = UITheme.campo();

    // ── Tabela ────────────────────────────────────────────────────────────────
    private final DefaultTableModel modeloTabela;
    private final JTable tabela;

    // ── Busca ─────────────────────────────────────────────────────────────────
    private final JTextField txtBusca = UITheme.campo();

    public ClientePanel(ClienteController controller) {
        this.controller = controller;
        setBackground(UITheme.C_BG);
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        modeloTabela = new DefaultTableModel(
                new String[]{"CPF", "Nome", "Endereço", "Telefone", "E-mail"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabela = UITheme.tabela(modeloTabela);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                construirFormulario(), construirLista());
        split.setDividerLocation(340);
        split.setBackground(UITheme.C_BG);
        split.setBorder(null);
        add(split, BorderLayout.CENTER);

        recarregarTabela();
        registrarListenerTabela();
    }

    // ── Formulário ────────────────────────────────────────────────────────────

    private JPanel construirFormulario() {
        JPanel card = UITheme.card("Cadastrar / Editar Cliente");

        JPanel grid = UITheme.gridPanel();
        GridBagConstraints g = UITheme.gbc();
        UITheme.addLinha(grid, g, 0, "CPF:",      txtCpf);
        UITheme.addLinha(grid, g, 1, "Nome:",     txtNome);
        UITheme.addLinha(grid, g, 2, "Endereço:", txtEndereco);
        UITheme.addLinha(grid, g, 3, "Telefone:", txtTelefone);
        UITheme.addLinha(grid, g, 4, "E-mail:",   txtEmail);

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
        JPanel card = UITheme.card("Clientes Cadastrados");

        txtBusca.setPreferredSize(new Dimension(200, 30));
        JButton btnBuscar = UITheme.botao("Buscar",      UITheme.C_ACCENT);
        JButton btnListar = UITheme.botao("Listar Todos", UITheme.C_SUCCESS);

        btnBuscar.addActionListener(e -> onBuscar());
        btnListar.addActionListener(e -> recarregarTabela());

        card.add(UITheme.painelBusca(
                UITheme.label("CPF:"), txtBusca, btnBuscar, btnListar),
                BorderLayout.NORTH);
        card.add(UITheme.scrollPane(tabela), BorderLayout.CENTER);
        return card;
    }

    // ── Handlers de eventos ───────────────────────────────────────────────────

    private void onSalvar() {
        try {
            controller.salvar(txtCpf.getText(), txtNome.getText(),
                    txtEndereco.getText(), txtTelefone.getText(), txtEmail.getText());
            UITheme.dialogo(this, "✅ Cliente salvo com sucesso!");
            limparFormulario();
            recarregarTabela();
        } catch (Exception ex) {
            UITheme.erro(this, ex.getMessage());
        }
    }

    private void onExcluir() {
        if (!UITheme.confirmar(this, "Confirmar exclusão do cliente CPF " + txtCpf.getText() + "?"))
            return;
        try {
            controller.excluir(txtCpf.getText());
            UITheme.dialogo(this, "Cliente excluído.");
            limparFormulario();
            recarregarTabela();
        } catch (Exception ex) {
            UITheme.erro(this, ex.getMessage());
        }
    }

    private void onBuscar() {
        try {
            Cliente c = controller.buscar(txtBusca.getText());
            modeloTabela.setRowCount(0);
            modeloTabela.addRow(new Object[]{
                    c.getCpf(), c.getNome(), c.getEndereco(), c.getTelefone(), c.getEmail()
            });
        } catch (Exception ex) {
            UITheme.erro(this, ex.getMessage());
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private void recarregarTabela() {
        modeloTabela.setRowCount(0);
        for (Cliente c : controller.listarTodos()) {
            modeloTabela.addRow(new Object[]{
                    c.getCpf(), c.getNome(), c.getEndereco(), c.getTelefone(), c.getEmail()
            });
        }
    }

    private void limparFormulario() {
        UITheme.limpar(txtCpf, txtNome, txtEndereco, txtTelefone, txtEmail);
    }

    /** Clique em linha preenche o formulário para edição. */
    private void registrarListenerTabela() {
        tabela.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = tabela.getSelectedRow();
                if (row < 0) return;
                txtCpf.setText(String.valueOf(modeloTabela.getValueAt(row, 0)));
                txtNome.setText((String) modeloTabela.getValueAt(row, 1));
                txtEndereco.setText((String) modeloTabela.getValueAt(row, 2));
                txtTelefone.setText((String) modeloTabela.getValueAt(row, 3));
                txtEmail.setText((String) modeloTabela.getValueAt(row, 4));
            }
        });
    }
}
