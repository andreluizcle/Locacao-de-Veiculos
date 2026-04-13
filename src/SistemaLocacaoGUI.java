import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class SistemaLocacaoGUI extends JFrame {

    // ── Paleta de cores ───────────────────────────────────────────────────────
    private static final Color C_BG       = new Color(15, 18, 25);
    private static final Color C_PANEL    = new Color(22, 27, 38);
    private static final Color C_CARD     = new Color(30, 37, 52);
    private static final Color C_BORDER   = new Color(48, 58, 80);
    private static final Color C_ACCENT   = new Color(56, 139, 253);
    private static final Color C_ACCENT2  = new Color(35, 197, 152);
    private static final Color C_DANGER   = new Color(220, 80, 80);
    private static final Color C_TEXT     = new Color(220, 230, 242);
    private static final Color C_MUTED    = new Color(130, 148, 175);
    private static final Font  F_TITLE    = new Font("SansSerif", Font.BOLD, 13);
    private static final Font  F_LABEL    = new Font("SansSerif", Font.PLAIN, 12);
    private static final Font  F_MONO     = new Font("Monospaced", Font.PLAIN, 12);

    public SistemaLocacaoGUI() {
        setTitle("Sistema de Locação de Veículos — FT UNICAMP");
        setSize(1000, 680);
        setMinimumSize(new Dimension(900, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(C_BG);

        // Cabeçalho
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(C_PANEL);
        header.setBorder(new MatteBorder(0, 0, 1, 0, C_BORDER));
        JLabel titulo = new JLabel("  🚗  Sistema de Locação de Veículos", JLabel.LEFT);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 16));
        titulo.setForeground(C_TEXT);
        titulo.setBorder(new EmptyBorder(14, 16, 14, 0));
        JLabel subtitulo = new JLabel("FT UNICAMP  ", JLabel.RIGHT);
        subtitulo.setFont(F_LABEL);
        subtitulo.setForeground(C_MUTED);
        subtitulo.setBorder(new EmptyBorder(14, 0, 14, 16));
        header.add(titulo, BorderLayout.WEST);
        header.add(subtitulo, BorderLayout.EAST);

        JTabbedPane abas = new JTabbedPane();
        abas.setBackground(C_BG);
        abas.setForeground(C_TEXT);
        abas.setFont(F_TITLE);
        estilizarAbas(abas);

        abas.addTab("👤  Clientes",          criarPainelCliente());
        abas.addTab("🚘  Frota",              criarPainelVeiculo());
        abas.addTab("📋  Locação/Devolução",  criarPainelLocacao());

        JPanel conteudo = new JPanel(new BorderLayout());
        conteudo.setBackground(C_BG);
        conteudo.add(header, BorderLayout.NORTH);
        conteudo.add(abas,   BorderLayout.CENTER);
        setContentPane(conteudo);
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  ABA CLIENTES
    // ══════════════════════════════════════════════════════════════════════════

    private JPanel criarPainelCliente() {
        JPanel raiz = new JPanel(new BorderLayout(10, 10));
        raiz.setBackground(C_BG);
        raiz.setBorder(new EmptyBorder(12, 12, 12, 12));

        // ─ Formulário de cadastro/edição ──────────────────────────────────────
        JPanel formCard = card("Cadastrar / Editar Cliente");

        JTextField txtCpf      = campo();
        JTextField txtNome     = campo();
        JTextField txtEndereco = campo();
        JTextField txtTelefone = campo();
        JTextField txtEmail    = campo();

        JPanel grid = new JPanel(new GridBagLayout());
        grid.setBackground(C_CARD);
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(5, 8, 5, 8);
        g.fill = GridBagConstraints.HORIZONTAL;

        adicionarLinhaCampo(grid, g, 0, "CPF:",      txtCpf);
        adicionarLinhaCampo(grid, g, 1, "Nome:",     txtNome);
        adicionarLinhaCampo(grid, g, 2, "Endereço:", txtEndereco);
        adicionarLinhaCampo(grid, g, 3, "Telefone:", txtTelefone);
        adicionarLinhaCampo(grid, g, 4, "E-mail:",   txtEmail);

        JButton btnSalvar  = botao("Salvar",  C_ACCENT);
        JButton btnExcluir = botao("Excluir", C_DANGER);
        JButton btnLimpar  = botao("Limpar",  C_BORDER);

        JPanel botoesForm = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        botoesForm.setBackground(C_CARD);
        botoesForm.add(btnLimpar);
        botoesForm.add(btnExcluir);
        botoesForm.add(btnSalvar);

        formCard.add(grid, BorderLayout.CENTER);
        formCard.add(botoesForm, BorderLayout.SOUTH);

        // ─ Tabela de consulta ─────────────────────────────────────────────────
        JPanel listaCard = card("Clientes Cadastrados");

        String[] colunas = {"CPF", "Nome", "Endereço", "Telefone", "E-mail"};
        DefaultTableModel modeloTabela = new DefaultTableModel(colunas, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tabela = tabela(modeloTabela);

        JTextField txtBusca = campo();
        txtBusca.setPreferredSize(new Dimension(220, 30));
        JButton btnBuscar  = botao("Buscar", C_ACCENT);
        JButton btnListar  = botao("Listar Todos", C_ACCENT2);

        JPanel barraConsulta = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        barraConsulta.setBackground(C_CARD);
        barraConsulta.add(label("CPF para buscar:"));
        barraConsulta.add(txtBusca);
        barraConsulta.add(btnBuscar);
        barraConsulta.add(btnListar);

        listaCard.add(barraConsulta, BorderLayout.NORTH);
        listaCard.add(new JScrollPane(tabela), BorderLayout.CENTER);

        // ─ Layout geral ───────────────────────────────────────────────────────
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, formCard, listaCard);
        split.setDividerLocation(340);
        split.setBackground(C_BG);
        split.setBorder(null);
        raiz.add(split, BorderLayout.CENTER);

        // ─ Ações ──────────────────────────────────────────────────────────────
        Runnable recarregarTabela = () -> {
            modeloTabela.setRowCount(0);
            for (Cliente c : RepositorioPersistencia.listarClientes()) {
                modeloTabela.addRow(new Object[]{
                        c.getCpfCliente(), c.getNomeCliente(),
                        c.getEnderecoCliente(), c.getTelefoneCliente(), c.getEmailCliente()
                });
            }
        };

        btnSalvar.addActionListener(e -> {
            try {
                long cpf = Long.parseLong(txtCpf.getText().trim());
                Cliente c = new Cliente(cpf, txtNome.getText().trim(),
                        txtEndereco.getText().trim(), txtTelefone.getText().trim(),
                        txtEmail.getText().trim());
                RepositorioPersistencia.salvarCliente(c);
                dialogo("✅ Cliente '" + c.getNomeCliente() + "' salvo com sucesso!");
                limparCampos(txtCpf, txtNome, txtEndereco, txtTelefone, txtEmail);
                recarregarTabela.run();
            } catch (NumberFormatException ex) {
                erro("CPF inválido — digite apenas números.");
            } catch (Exception ex) {
                erro("Erro: " + ex.getMessage());
            }
        });

        btnExcluir.addActionListener(e -> {
            try {
                long cpf = Long.parseLong(txtCpf.getText().trim());
                int conf = JOptionPane.showConfirmDialog(this,
                        "Confirmar exclusão do cliente CPF " + cpf + "?",
                        "Confirmar", JOptionPane.YES_NO_OPTION);
                if (conf == JOptionPane.YES_OPTION) {
                    RepositorioPersistencia.excluirCliente(cpf);
                    dialogo("Cliente excluído.");
                    limparCampos(txtCpf, txtNome, txtEndereco, txtTelefone, txtEmail);
                    recarregarTabela.run();
                }
            } catch (NumberFormatException ex) {
                erro("CPF inválido.");
            }
        });

        btnLimpar.addActionListener(e ->
                limparCampos(txtCpf, txtNome, txtEndereco, txtTelefone, txtEmail));

        btnBuscar.addActionListener(e -> {
            try {
                long cpf = Long.parseLong(txtBusca.getText().trim());
                Cliente c = RepositorioPersistencia.buscarCliente(cpf);
                modeloTabela.setRowCount(0);
                if (c != null) {
                    modeloTabela.addRow(new Object[]{
                            c.getCpfCliente(), c.getNomeCliente(),
                            c.getEnderecoCliente(), c.getTelefoneCliente(), c.getEmailCliente()
                    });
                } else {
                    dialogo("Nenhum cliente encontrado com CPF " + cpf + ".");
                }
            } catch (NumberFormatException ex) {
                erro("CPF inválido.");
            }
        });

        btnListar.addActionListener(e -> recarregarTabela.run());

        // Clique na linha preenche o formulário
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

        // Carrega ao abrir
        recarregarTabela.run();
        return raiz;
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  ABA FROTA (VEÍCULOS)
    // ══════════════════════════════════════════════════════════════════════════

    private JPanel criarPainelVeiculo() {
        JPanel raiz = new JPanel(new BorderLayout(10, 10));
        raiz.setBackground(C_BG);
        raiz.setBorder(new EmptyBorder(12, 12, 12, 12));

        JPanel formCard = card("Cadastrar / Editar Veículo");

        JTextField txtPlaca     = campo();
        JTextField txtRenavam   = campo();
        JTextField txtChassi    = campo();
        JTextField txtCor       = campo();
        JTextField txtPortas    = campo();
        JTextField txtKm        = campo();
        JTextField txtValor     = campo();
        JTextField txtModelo    = campo();
        JTextField txtMarca     = campo();
        JComboBox<String> cbComb = new JComboBox<>(new String[]{
                "1 - Gasolina", "2 - Etanol", "3 - Diesel", "4 - Flex", "5 - Elétrico"});
        estilizarCombo(cbComb);

        JPanel grid = new JPanel(new GridBagLayout());
        grid.setBackground(C_CARD);
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(4, 8, 4, 8);
        g.fill = GridBagConstraints.HORIZONTAL;

        adicionarLinhaCampo(grid, g, 0,  "Placa:",         txtPlaca);
        adicionarLinhaCampo(grid, g, 1,  "RENAVAM:",       txtRenavam);
        adicionarLinhaCampo(grid, g, 2,  "Chassi:",        txtChassi);
        adicionarLinhaCampo(grid, g, 3,  "Cor:",           txtCor);
        adicionarLinhaCampo(grid, g, 4,  "Nº Portas:",     txtPortas);
        adicionarLinhaCombo(grid, g, 5,  "Combustível:",   cbComb);
        adicionarLinhaCampo(grid, g, 6,  "KM atual:",      txtKm);
        adicionarLinhaCampo(grid, g, 7,  "Valor/Diária R$:", txtValor);
        adicionarLinhaCampo(grid, g, 8,  "Modelo:",        txtModelo);
        adicionarLinhaCampo(grid, g, 9,  "Marca:",         txtMarca);

        JButton btnSalvar  = botao("Salvar",  C_ACCENT);
        JButton btnExcluir = botao("Excluir", C_DANGER);
        JButton btnLimpar  = botao("Limpar",  C_BORDER);

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        botoes.setBackground(C_CARD);
        botoes.add(btnLimpar); botoes.add(btnExcluir); botoes.add(btnSalvar);

        formCard.add(grid, BorderLayout.CENTER);
        formCard.add(botoes, BorderLayout.SOUTH);

        // ─ Tabela ─────────────────────────────────────────────────────────────
        JPanel listaCard = card("Frota Cadastrada");

        String[] colunas = {"Placa", "Marca", "Modelo", "Cor", "KM", "Valor/Dia"};
        DefaultTableModel modeloTabela = new DefaultTableModel(colunas, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tabela = tabela(modeloTabela);

        JTextField txtBuscaPlaca = campo();
        txtBuscaPlaca.setPreferredSize(new Dimension(160, 30));
        JButton btnBuscar = botao("Buscar",     C_ACCENT);
        JButton btnListar = botao("Listar Todos", C_ACCENT2);

        JPanel barra = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        barra.setBackground(C_CARD);
        barra.add(label("Placa:")); barra.add(txtBuscaPlaca);
        barra.add(btnBuscar); barra.add(btnListar);

        listaCard.add(barra, BorderLayout.NORTH);
        listaCard.add(new JScrollPane(tabela), BorderLayout.CENTER);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, formCard, listaCard);
        split.setDividerLocation(380);
        split.setBackground(C_BG);
        split.setBorder(null);
        raiz.add(split, BorderLayout.CENTER);

        // ─ Ações ──────────────────────────────────────────────────────────────
        Runnable recarregar = () -> {
            modeloTabela.setRowCount(0);
            for (Automovel a : RepositorioPersistencia.listarAutomoveis()) {
                modeloTabela.addRow(new Object[]{
                        a.getPlacaAutomovel(),
                        a.getModelo().getMarca().getDescricao(),
                        a.getModelo().getDescricaoModelo(),
                        a.getCorAutomovel(),
                        a.getQuilometragemAutomovel(),
                        "R$ " + String.format("%.2f", a.getValorLocacaoAutomovel())
                });
            }
        };

        btnSalvar.addActionListener(e -> {
            try {
                String placa = txtPlaca.getText().trim().toUpperCase();
                if (placa.isEmpty()) { erro("Placa obrigatória."); return; }
                Marca  marca  = new Marca(txtMarca.getText().trim());
                Modelo modelo = new Modelo(txtModelo.getText().trim(), marca);
                int comb = cbComb.getSelectedIndex() + 1;
                Automovel a = new Automovel(placa,
                        Long.parseLong(txtRenavam.getText().trim()),
                        txtChassi.getText().trim(),
                        txtCor.getText().trim(),
                        Integer.parseInt(txtPortas.getText().trim()),
                        comb,
                        Long.parseLong(txtKm.getText().trim()),
                        Double.parseDouble(txtValor.getText().trim()),
                        modelo);
                RepositorioPersistencia.salvarAutomovel(a);
                dialogo("✅ Veículo '" + placa + "' salvo com sucesso!");
                limparCampos(txtPlaca, txtRenavam, txtChassi, txtCor, txtPortas, txtKm, txtValor, txtModelo, txtMarca);
                recarregar.run();
            } catch (NumberFormatException ex) {
                erro("Verifique os campos numéricos (RENAVAM, Portas, KM, Valor).");
            } catch (Exception ex) {
                erro("Erro: " + ex.getMessage());
            }
        });

        btnExcluir.addActionListener(e -> {
            String placa = txtPlaca.getText().trim().toUpperCase();
            if (placa.isEmpty()) { erro("Informe a placa para excluir."); return; }
            int conf = JOptionPane.showConfirmDialog(this,
                    "Excluir veículo " + placa + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (conf == JOptionPane.YES_OPTION) {
                RepositorioPersistencia.excluirAutomovel(placa);
                dialogo("Veículo excluído.");
                limparCampos(txtPlaca, txtRenavam, txtChassi, txtCor, txtPortas, txtKm, txtValor, txtModelo, txtMarca);
                recarregar.run();
            }
        });

        btnLimpar.addActionListener(e ->
                limparCampos(txtPlaca, txtRenavam, txtChassi, txtCor, txtPortas, txtKm, txtValor, txtModelo, txtMarca));

        btnBuscar.addActionListener(e -> {
            String placa = txtBuscaPlaca.getText().trim().toUpperCase();
            Automovel a = RepositorioPersistencia.buscarAutomovel(placa);
            modeloTabela.setRowCount(0);
            if (a != null) {
                modeloTabela.addRow(new Object[]{
                        a.getPlacaAutomovel(), a.getModelo().getMarca().getDescricao(),
                        a.getModelo().getDescricaoModelo(), a.getCorAutomovel(),
                        a.getQuilometragemAutomovel(),
                        "R$ " + String.format("%.2f", a.getValorLocacaoAutomovel())
                });
            } else {
                dialogo("Nenhum veículo encontrado com a placa " + placa + ".");
            }
        });

        btnListar.addActionListener(e -> recarregar.run());

        tabela.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = tabela.getSelectedRow();
                if (row < 0) return;
                String placa = String.valueOf(modeloTabela.getValueAt(row, 0));
                Automovel a = RepositorioPersistencia.buscarAutomovel(placa);
                if (a == null) return;
                txtPlaca.setText(a.getPlacaAutomovel());
                txtRenavam.setText(String.valueOf(a.getRenavamAutomovel()));
                txtChassi.setText(a.getChassiAutomovel());
                txtCor.setText(a.getCorAutomovel());
                txtPortas.setText(String.valueOf(a.getNumeroPortasAutomovel()));
                txtKm.setText(String.valueOf(a.getQuilometragemAutomovel()));
                txtValor.setText(String.valueOf(a.getValorLocacaoAutomovel()));
                txtModelo.setText(a.getModelo().getDescricaoModelo());
                txtMarca.setText(a.getModelo().getMarca().getDescricao());
                cbComb.setSelectedIndex(a.getTipoCombustivelAutomovel() - 1);
            }
        });

        recarregar.run();
        return raiz;
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  ABA LOCAÇÃO / DEVOLUÇÃO
    // ══════════════════════════════════════════════════════════════════════════

    private JPanel criarPainelLocacao() {
        JPanel raiz = new JPanel(new BorderLayout(10, 10));
        raiz.setBackground(C_BG);
        raiz.setBorder(new EmptyBorder(12, 12, 12, 12));

        // ─ Painel Nova Locação ────────────────────────────────────────────────
        JPanel cardLocacao = card("Nova Locação (Saída)");

        JTextField txtCpfLoc   = campo();
        JTextField txtPlacaLoc = campo();

        JPanel gridLoc = new JPanel(new GridBagLayout());
        gridLoc.setBackground(C_CARD);
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6, 8, 6, 8);
        g.fill = GridBagConstraints.HORIZONTAL;

        adicionarLinhaCampo(gridLoc, g, 0, "CPF do Cliente:", txtCpfLoc);
        adicionarLinhaCampo(gridLoc, g, 1, "Placa do Veículo:", txtPlacaLoc);

        JButton btnLocar = botao("Registrar Locação", C_ACCENT2);

        JPanel botLoc = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        botLoc.setBackground(C_CARD);
        botLoc.add(btnLocar);

        cardLocacao.add(gridLoc, BorderLayout.CENTER);
        cardLocacao.add(botLoc, BorderLayout.SOUTH);

        // ─ Painel Devolução ───────────────────────────────────────────────────
        JPanel cardDev = card("Registrar Devolução");

        JTextField txtIdLocacao = campo();
        JTextField txtKmDev     = campo();

        JPanel gridDev = new JPanel(new GridBagLayout());
        gridDev.setBackground(C_CARD);
        GridBagConstraints gd = new GridBagConstraints();
        gd.insets = new Insets(6, 8, 6, 8);
        gd.fill = GridBagConstraints.HORIZONTAL;

        adicionarLinhaCampo(gridDev, gd, 0, "ID da Locação:", txtIdLocacao);
        adicionarLinhaCampo(gridDev, gd, 1, "KM Final:", txtKmDev);

        JButton btnDevolver = botao("Registrar Devolução", C_DANGER);

        JPanel botDev = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        botDev.setBackground(C_CARD);
        botDev.add(btnDevolver);

        cardDev.add(gridDev, BorderLayout.CENTER);
        cardDev.add(botDev, BorderLayout.SOUTH);

        // ─ Tabela de locações ─────────────────────────────────────────────────
        JPanel listaCard = card("Histórico de Locações");

        String[] colunas = {"ID", "Cliente", "Veículo", "Data Saída", "Data Devolução", "Status", "Valor Total"};
        DefaultTableModel modeloTabela = new DefaultTableModel(colunas, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tabela = tabela(modeloTabela);

        JButton btnListar = botao("Atualizar Lista", C_ACCENT);
        JButton btnSoAtivas = botao("Só Ativas", C_ACCENT2);

        JPanel barra = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        barra.setBackground(C_CARD);
        barra.add(btnListar);
        barra.add(btnSoAtivas);

        listaCard.add(barra, BorderLayout.NORTH);
        listaCard.add(new JScrollPane(tabela), BorderLayout.CENTER);

        // ─ Layout ─────────────────────────────────────────────────────────────
        JPanel painelEsquerdo = new JPanel(new GridLayout(2, 1, 0, 10));
        painelEsquerdo.setBackground(C_BG);
        painelEsquerdo.add(cardLocacao);
        painelEsquerdo.add(cardDev);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, painelEsquerdo, listaCard);
        split.setDividerLocation(360);
        split.setBackground(C_BG);
        split.setBorder(null);
        raiz.add(split, BorderLayout.CENTER);

        // ─ Ações ──────────────────────────────────────────────────────────────
        Runnable recarregar = () -> {
            modeloTabela.setRowCount(0);
            for (Locacao l : RepositorioPersistencia.listarLocacoes()) {
                modeloTabela.addRow(new Object[]{
                        l.getIdLocacao(),
                        l.getCliente().getNomeCliente(),
                        l.getAutomovel().getPlacaAutomovel(),
                        l.getDataLocacao(),
                        l.getDataDevolucao() != null ? l.getDataDevolucao() : "—",
                        l.getStatus(),
                        l.getValorTotal() > 0 ? "R$ " + String.format("%.2f", l.getValorTotal()) : "—"
                });
            }
        };

        btnLocar.addActionListener(e -> {
            try {
                long cpf = Long.parseLong(txtCpfLoc.getText().trim());
                String placa = txtPlacaLoc.getText().trim().toUpperCase();

                Cliente cliente = RepositorioPersistencia.buscarCliente(cpf);
                if (cliente == null) { erro("Cliente com CPF " + cpf + " não encontrado."); return; }

                Automovel auto = RepositorioPersistencia.buscarAutomovel(placa);
                if (auto == null) { erro("Veículo com placa " + placa + " não encontrado."); return; }

                // Verifica se o veículo já está locado
                boolean jaLocado = RepositorioPersistencia.listarLocacoes().stream()
                        .anyMatch(l -> l.getAutomovel().getPlacaAutomovel().equalsIgnoreCase(placa)
                                && l.getStatus() == Locacao.StatusLocacao.ATIVA);
                if (jaLocado) { erro("Este veículo já possui uma locação ativa!"); return; }

                Locacao loc = new Locacao(LocalDate.now(), LocalTime.now(), cliente, auto);
                loc.registrarLocacao();

                dialogo("✅ Locação #" + loc.getIdLocacao() + " registrada!\n"
                        + "Cliente: " + cliente.getNomeCliente() + "\n"
                        + "Veículo: " + placa);
                limparCampos(txtCpfLoc, txtPlacaLoc);
                recarregar.run();
            } catch (NumberFormatException ex) {
                erro("CPF inválido.");
            } catch (Exception ex) {
                erro("Erro: " + ex.getMessage());
            }
        });

        btnDevolver.addActionListener(e -> {
            try {
                long id = Long.parseLong(txtIdLocacao.getText().trim());
                long km = Long.parseLong(txtKmDev.getText().trim());

                Locacao loc = RepositorioPersistencia.buscarLocacao(id);
                if (loc == null) { erro("Locação #" + id + " não encontrada."); return; }
                if (loc.getStatus() != Locacao.StatusLocacao.ATIVA) {
                    erro("Esta locação não está ativa."); return;
                }

                loc.registrarDevolucao(LocalDate.now(), LocalTime.now(), km);

                dialogo("✅ Devolução registrada!\n"
                        + "Locação #" + id + "\n"
                        + "Veículo: " + loc.getAutomovel().getPlacaAutomovel() + "\n"
                        + "Valor total: R$ " + String.format("%.2f", loc.getValorTotal()));
                limparCampos(txtIdLocacao, txtKmDev);
                recarregar.run();
            } catch (NumberFormatException ex) {
                erro("ID e KM devem ser números inteiros.");
            } catch (Exception ex) {
                erro("Erro: " + ex.getMessage());
            }
        });

        btnListar.addActionListener(e -> recarregar.run());

        btnSoAtivas.addActionListener(e -> {
            modeloTabela.setRowCount(0);
            for (Locacao l : RepositorioPersistencia.listarLocacoes()) {
                if (l.getStatus() == Locacao.StatusLocacao.ATIVA) {
                    modeloTabela.addRow(new Object[]{
                            l.getIdLocacao(),
                            l.getCliente().getNomeCliente(),
                            l.getAutomovel().getPlacaAutomovel(),
                            l.getDataLocacao(), "—", l.getStatus(), "—"
                    });
                }
            }
        });

        // Clique na linha preenche o campo de devolução
        tabela.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = tabela.getSelectedRow();
                if (row < 0) return;
                txtIdLocacao.setText(String.valueOf(modeloTabela.getValueAt(row, 0)));
            }
        });

        recarregar.run();
        return raiz;
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  UTILITÁRIOS DE INTERFACE
    // ══════════════════════════════════════════════════════════════════════════

    private JPanel card(String titulo) {
        JPanel p = new JPanel(new BorderLayout(0, 8));
        p.setBackground(C_CARD);
        p.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(C_BORDER, 1, true),
                new EmptyBorder(12, 12, 12, 12)));
        JLabel lbl = new JLabel(titulo);
        lbl.setFont(F_TITLE);
        lbl.setForeground(C_ACCENT);
        lbl.setBorder(new EmptyBorder(0, 0, 6, 0));
        p.add(lbl, BorderLayout.NORTH);
        return p;
    }

    private JTextField campo() {
        JTextField tf = new JTextField();
        tf.setBackground(new Color(12, 15, 22));
        tf.setForeground(C_TEXT);
        tf.setCaretColor(C_ACCENT);
        tf.setFont(F_MONO);
        tf.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(C_BORDER, 1),
                new EmptyBorder(4, 8, 4, 8)));
        tf.setPreferredSize(new Dimension(200, 30));
        return tf;
    }

    private JLabel label(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(F_LABEL);
        lbl.setForeground(C_MUTED);
        return lbl;
    }

    private JButton botao(String texto, Color cor) {
        JButton btn = new JButton(texto);
        btn.setFont(F_TITLE);
        btn.setBackground(cor);
        btn.setForeground(Color.WHITE);
        btn.setBorder(new EmptyBorder(7, 16, 7, 16));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(cor.brighter());
            }
            public void mouseExited(MouseEvent e) {
                btn.setBackground(cor);
            }
        });
        return btn;
    }

    private JTable tabela(DefaultTableModel modelo) {
        JTable t = new JTable(modelo);
        t.setBackground(C_CARD);
        t.setForeground(C_TEXT);
        t.setFont(F_LABEL);
        t.setGridColor(C_BORDER);
        t.setRowHeight(26);
        t.setSelectionBackground(C_ACCENT.darker());
        t.setSelectionForeground(Color.WHITE);
        t.getTableHeader().setBackground(C_PANEL);
        t.getTableHeader().setForeground(C_MUTED);
        t.getTableHeader().setFont(F_TITLE);
        t.getTableHeader().setBorder(new MatteBorder(0, 0, 1, 0, C_BORDER));
        JScrollPane sp = new JScrollPane(t);
        sp.setBackground(C_CARD);
        sp.getViewport().setBackground(C_CARD);
        sp.setBorder(new LineBorder(C_BORDER, 1));
        return t;
    }

    private void adicionarLinhaCampo(JPanel grid, GridBagConstraints g,
                                     int row, String rotulo, JTextField campo) {
        g.gridx = 0; g.gridy = row; g.weightx = 0.3;
        grid.add(label(rotulo), g);
        g.gridx = 1; g.weightx = 0.7;
        grid.add(campo, g);
    }

    private void adicionarLinhaCombo(JPanel grid, GridBagConstraints g,
                                     int row, String rotulo, JComboBox<?> combo) {
        g.gridx = 0; g.gridy = row; g.weightx = 0.3;
        grid.add(label(rotulo), g);
        g.gridx = 1; g.weightx = 0.7;
        grid.add(combo, g);
    }

    private void estilizarCombo(JComboBox<?> cb) {
        cb.setBackground(new Color(12, 15, 22));
        cb.setForeground(C_TEXT);
        cb.setFont(F_MONO);
        cb.setBorder(new LineBorder(C_BORDER, 1));
    }

    private void estilizarAbas(JTabbedPane abas) {
        abas.setUI(new javax.swing.plaf.basic.BasicTabbedPaneUI() {
            protected void installDefaults() {
                super.installDefaults();
                highlight = C_PANEL;
                lightHighlight = C_PANEL;
                shadow = C_BG;
                darkShadow = C_BG;
                focus = C_ACCENT;
            }
        });
        UIManager.put("TabbedPane.selected", C_CARD);
        UIManager.put("TabbedPane.background", C_PANEL);
        UIManager.put("TabbedPane.foreground", C_TEXT);
        UIManager.put("TabbedPane.contentAreaColor", C_BG);
    }

    private void limparCampos(JTextField... campos) {
        for (JTextField f : campos) f.setText("");
    }

    private void dialogo(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Informação",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void erro(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Erro",
                JOptionPane.ERROR_MESSAGE);
    }

    // ══════════════════════════════════════════════════════════════════════════

    public static void main(String[] args) {
        // Aplica tema escuro globalmente
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        UIManager.put("OptionPane.background",   new Color(22, 27, 38));
        UIManager.put("Panel.background",         new Color(22, 27, 38));
        UIManager.put("OptionPane.messageForeground", new Color(220, 230, 242));

        SwingUtilities.invokeLater(() -> new SistemaLocacaoGUI().setVisible(true));
    }
}