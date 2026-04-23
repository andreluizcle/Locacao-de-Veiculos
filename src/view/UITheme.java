package view;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Centraliza a paleta de cores, fontes e métodos de fábrica de componentes visuais.
 *
 * Todos os painéis da View usam esta classe para criar seus componentes,
 * garantindo consistência visual sem duplicar código de estilo.
 */
public final class UITheme {

    // ── Paleta ────────────────────────────────────────────────────────────────
    public static final Color C_BG      = new Color(15,  18,  25);
    public static final Color C_PANEL   = new Color(22,  27,  38);
    public static final Color C_CARD    = new Color(30,  37,  52);
    public static final Color C_BORDER  = new Color(48,  58,  80);
    public static final Color C_ACCENT  = new Color(56,  139, 253);
    public static final Color C_SUCCESS = new Color(35,  197, 152);
    public static final Color C_DANGER  = new Color(220, 80,  80);
    public static final Color C_TEXT    = new Color(220, 230, 242);
    public static final Color C_MUTED   = new Color(130, 148, 175);
    public static final Color C_INPUT   = new Color(12,  15,  22);

    // ── Fontes ────────────────────────────────────────────────────────────────
    public static final Font F_TITULO = new Font("SansSerif", Font.BOLD,  16);
    public static final Font F_SECAO  = new Font("SansSerif", Font.BOLD,  13);
    public static final Font F_LABEL  = new Font("SansSerif", Font.PLAIN, 12);
    public static final Font F_MONO   = new Font("Monospaced", Font.PLAIN, 12);

    private UITheme() {} // utilitária — não instanciar

    // ── Fábricas de componentes ───────────────────────────────────────────────

    /** Card com título colorido e borda arredondada. */
    public static JPanel card(String titulo) {
        JPanel p = new JPanel(new BorderLayout(0, 8));
        p.setBackground(C_CARD);
        p.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(C_BORDER, 1, true),
                new EmptyBorder(12, 12, 12, 12)));
        JLabel lbl = new JLabel(titulo);
        lbl.setFont(F_SECAO);
        lbl.setForeground(C_ACCENT);
        lbl.setBorder(new EmptyBorder(0, 0, 6, 0));
        p.add(lbl, BorderLayout.NORTH);
        return p;
    }

    /** Campo de texto estilizado. */
    public static JTextField campo() {
        JTextField tf = new JTextField();
        tf.setBackground(C_INPUT);
        tf.setForeground(C_TEXT);
        tf.setCaretColor(C_ACCENT);
        tf.setFont(F_MONO);
        tf.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(C_BORDER, 1),
                new EmptyBorder(4, 8, 4, 8)));
        tf.setPreferredSize(new Dimension(200, 30));
        return tf;
    }

    /** Rótulo estilizado. */
    public static JLabel label(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(F_LABEL);
        lbl.setForeground(C_MUTED);
        return lbl;
    }

    /** Botão com hover effect. */
    public static JButton botao(String texto, Color cor) {
        JButton btn = new JButton(texto);
        btn.setFont(F_SECAO);
        btn.setBackground(cor);
        btn.setForeground(Color.WHITE);
        btn.setBorder(new EmptyBorder(7, 16, 7, 16));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(cor.brighter()); }
            public void mouseExited(MouseEvent e)  { btn.setBackground(cor); }
        });
        return btn;
    }

    /** Tabela com tema escuro. Retorna a JTable; o JScrollPane fica interno. */
    public static JTable tabela(DefaultTableModel modelo) {
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
        t.getTableHeader().setFont(F_SECAO);
        t.getTableHeader().setBorder(new MatteBorder(0, 0, 1, 0, C_BORDER));
        return t;
    }

    /** JScrollPane estilizado para envolver uma tabela. */
    public static JScrollPane scrollPane(JTable tabela) {
        JScrollPane sp = new JScrollPane(tabela);
        sp.setBackground(C_CARD);
        sp.getViewport().setBackground(C_CARD);
        sp.setBorder(new LineBorder(C_BORDER, 1));
        return sp;
    }

    /** ComboBox estilizado. */
    public static void estilizarCombo(JComboBox<?> cb) {
        cb.setBackground(C_INPUT);
        cb.setForeground(C_TEXT);
        cb.setFont(F_MONO);
        cb.setBorder(new LineBorder(C_BORDER, 1));
    }

    /** Panel com GridBagLayout e background do card. */
    public static JPanel gridPanel() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(C_CARD);
        return p;
    }

    /** Constraints padrão para formulários em grid. */
    public static GridBagConstraints gbc() {
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(5, 8, 5, 8);
        g.fill = GridBagConstraints.HORIZONTAL;
        return g;
    }

    /** Adiciona par (rótulo, campo) em uma linha do grid. */
    public static void addLinha(JPanel grid, GridBagConstraints g,
                                 int row, String rotulo, JComponent campo) {
        g.gridx = 0; g.gridy = row; g.weightx = 0.3;
        grid.add(label(rotulo), g);
        g.gridx = 1; g.weightx = 0.7;
        grid.add(campo, g);
    }

    /** Painel de botões alinhados à direita. */
    public static JPanel painelBotoes(JButton... botoes) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        p.setBackground(C_CARD);
        for (JButton b : botoes) p.add(b);
        return p;
    }

    /** Painel de busca alinhado à esquerda. */
    public static JPanel painelBusca(JComponent... componentes) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        p.setBackground(C_CARD);
        for (JComponent c : componentes) p.add(c);
        return p;
    }

    /** Aplica o tema escuro às abas. */
    public static void estilizarAbas(JTabbedPane abas) {
        UIManager.put("TabbedPane.selected",         C_CARD);
        UIManager.put("TabbedPane.background",        C_PANEL);
        UIManager.put("TabbedPane.foreground",        C_TEXT);
        UIManager.put("TabbedPane.contentAreaColor",  C_BG);
        abas.setBackground(C_BG);
        abas.setForeground(C_TEXT);
        abas.setFont(F_SECAO);
    }

    /** Exibe diálogo informativo. */
    public static void dialogo(Component pai, String msg) {
        JOptionPane.showMessageDialog(pai, msg, "Informação", JOptionPane.INFORMATION_MESSAGE);
    }

    /** Exibe diálogo de erro. */
    public static void erro(Component pai, String msg) {
        JOptionPane.showMessageDialog(pai, msg, "Erro", JOptionPane.ERROR_MESSAGE);
    }

    /** Solicita confirmação sim/não. */
    public static boolean confirmar(Component pai, String msg) {
        return JOptionPane.showConfirmDialog(pai, msg, "Confirmar",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }

    /** Limpa o texto de um ou mais campos. */
    public static void limpar(JTextField... campos) {
        for (JTextField f : campos) f.setText("");
    }
}
