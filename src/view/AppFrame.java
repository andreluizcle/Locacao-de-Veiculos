package view;

import controller.AutomovelController;
import controller.ClienteController;
import controller.LocacaoController;
import repository.*;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

/**
 * Janela principal da aplicação.
 *
 * Única responsabilidade desta classe: montar as abas e conectar as camadas
 * através de injeção de dependência — sem nenhuma lógica de negócio.
 *
 * Fluxo de construção:
 *   1. Cria os repositórios (implementações CSV).
 *   2. Cria os controllers, passando os repositórios necessários.
 *   3. Cria os painéis (View), passando apenas os controllers.
 *   4. Monta a janela com os painéis.
 *
 * Para trocar a camada de persistência (ex.: banco de dados), basta
 * substituir as implementações no passo 1 — nada mais precisa mudar.
 */
public class AppFrame extends JFrame {

    public AppFrame() {
        configurarJanela();

        // ── 1. Repositórios ───────────────────────────────────────────────────
        ClienteRepository   clienteRepo   = new ClienteCsvRepository("dados/clientes.csv");
        AutomovelRepository automovelRepo = new AutomovelCsvRepository("dados/automoveis.csv");
        LocacaoRepository   locacaoRepo   = new LocacaoCsvRepository(
                "dados/locacoes.csv", clienteRepo, automovelRepo);

        // ── 2. Controllers ────────────────────────────────────────────────────
        ClienteController   clienteCtrl   = new ClienteController(clienteRepo);
        AutomovelController automovelCtrl = new AutomovelController(automovelRepo, locacaoRepo);
        LocacaoController   locacaoCtrl   = new LocacaoController(locacaoRepo, clienteRepo, automovelRepo);

        // ── 3. Painéis (Views) ────────────────────────────────────────────────
        ClientePanel  painelCliente  = new ClientePanel(clienteCtrl);
        AutomovelPanel painelVeiculo = new AutomovelPanel(automovelCtrl);
        LocacaoPanel  painelLocacao  = new LocacaoPanel(locacaoCtrl);

        // ── 4. Layout ─────────────────────────────────────────────────────────
        JTabbedPane abas = new JTabbedPane();
        UITheme.estilizarAbas(abas);
        abas.addTab("👤  Clientes",         painelCliente);
        abas.addTab("🚘  Frota",             painelVeiculo);
        abas.addTab("📋  Locação/Devolução", painelLocacao);

        JPanel conteudo = new JPanel(new BorderLayout());
        conteudo.setBackground(UITheme.C_BG);
        conteudo.add(construirHeader(), BorderLayout.NORTH);
        conteudo.add(abas,             BorderLayout.CENTER);
        setContentPane(conteudo);
    }

    // ── Header ────────────────────────────────────────────────────────────────

    private JPanel construirHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(UITheme.C_PANEL);
        header.setBorder(new MatteBorder(0, 0, 1, 0, UITheme.C_BORDER));

        JLabel titulo = new JLabel("  🚗  Sistema de Locação de Veículos", JLabel.LEFT);
        titulo.setFont(UITheme.F_TITULO);
        titulo.setForeground(UITheme.C_TEXT);
        titulo.setBorder(new EmptyBorder(14, 16, 14, 0));

        JLabel subtitulo = new JLabel("FT UNICAMP  ", JLabel.RIGHT);
        subtitulo.setFont(UITheme.F_LABEL);
        subtitulo.setForeground(UITheme.C_MUTED);
        subtitulo.setBorder(new EmptyBorder(14, 0, 14, 16));

        header.add(titulo,    BorderLayout.WEST);
        header.add(subtitulo, BorderLayout.EAST);
        return header;
    }

    // ── Configuração da janela ────────────────────────────────────────────────

    private void configurarJanela() {
        setTitle("Sistema de Locação de Veículos — FT UNICAMP");
        setSize(1000, 680);
        setMinimumSize(new Dimension(900, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(UITheme.C_BG);
    }

    // ── Entry point ───────────────────────────────────────────────────────────

    public static void main(String[] args) {
        aplicarTemaGlobal();
        SwingUtilities.invokeLater(() -> new AppFrame().setVisible(true));
    }

    private static void aplicarTemaGlobal() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        UIManager.put("OptionPane.background",        new Color(22, 27, 38));
        UIManager.put("Panel.background",              new Color(22, 27, 38));
        UIManager.put("OptionPane.messageForeground",  new Color(220, 230, 242));
    }
}
