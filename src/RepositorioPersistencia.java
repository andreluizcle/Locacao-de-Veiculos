import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Camada de persistência baseada em arquivos CSV.
 * Cada entidade tem seu próprio arquivo na pasta "dados/".
 *
 * Formato dos arquivos:
 *   clientes.csv   → cpf;nome;endereco;telefone;email
 *   automoveis.csv → placa;renavam;chassi;cor;portas;combustivel;km;valorDiaria;descModelo;descMarca
 *   locacoes.csv   → id;dataLocacao;horaLocacao;dataDevolucao;horaDevolucao;kmFinal;valorTotal;status;cpfCliente;placaAutomovel
 */
public class RepositorioPersistencia {

    private static final String DIR   = "dados";
    private static final String ARQ_CLIENTES   = DIR + File.separator + "clientes.csv";
    private static final String ARQ_AUTOMOVEIS = DIR + File.separator + "automoveis.csv";
    private static final String ARQ_LOCACOES   = DIR + File.separator + "locacoes.csv";
    private static final String SEP = ";";

    static {
        new File(DIR).mkdirs();
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  CLIENTES
    // ══════════════════════════════════════════════════════════════════════════

    public static void salvarCliente(Cliente c) {
        List<Cliente> todos = listarClientes();
        // Evita duplicata por CPF
        todos.removeIf(x -> x.getCpfCliente() == c.getCpfCliente());
        todos.add(c);
        escreverClientes(todos);
    }

    public static void atualizarCliente(Cliente c) {
        salvarCliente(c); // upsert por CPF
    }

    public static void excluirCliente(long cpf) {
        List<Cliente> todos = listarClientes();
        todos.removeIf(x -> x.getCpfCliente() == cpf);
        escreverClientes(todos);
    }

    public static Cliente buscarCliente(long cpf) {
        return listarClientes().stream()
                .filter(c -> c.getCpfCliente() == cpf)
                .findFirst().orElse(null);
    }

    public static List<Cliente> listarClientes() {
        List<Cliente> lista = new ArrayList<>();
        File f = new File(ARQ_CLIENTES);
        if (!f.exists()) return lista;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                if (linha.isBlank()) continue;
                String[] p = linha.split(SEP, -1);
                if (p.length < 5) continue;
                lista.add(new Cliente(Long.parseLong(p[0]), p[1], p[2], p[3], p[4]));
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler clientes: " + e.getMessage());
        }
        return lista;
    }

    private static void escreverClientes(List<Cliente> lista) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ARQ_CLIENTES, false))) {
            for (Cliente c : lista) {
                pw.println(c.getCpfCliente() + SEP + c.getNomeCliente() + SEP
                        + c.getEnderecoCliente() + SEP + c.getTelefoneCliente() + SEP
                        + c.getEmailCliente());
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar clientes: " + e.getMessage());
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  AUTOMÓVEIS
    // ══════════════════════════════════════════════════════════════════════════

    public static void salvarAutomovel(Automovel a) {
        List<Automovel> todos = listarAutomoveis();
        todos.removeIf(x -> x.getPlacaAutomovel().equalsIgnoreCase(a.getPlacaAutomovel()));
        todos.add(a);
        escreverAutomoveis(todos);
    }

    public static void atualizarAutomovel(Automovel a) {
        salvarAutomovel(a);
    }

    public static void excluirAutomovel(String placa) {
        List<Automovel> todos = listarAutomoveis();
        todos.removeIf(x -> x.getPlacaAutomovel().equalsIgnoreCase(placa));
        escreverAutomoveis(todos);
    }

    public static Automovel buscarAutomovel(String placa) {
        return listarAutomoveis().stream()
                .filter(a -> a.getPlacaAutomovel().equalsIgnoreCase(placa))
                .findFirst().orElse(null);
    }

    public static List<Automovel> listarAutomoveis() {
        List<Automovel> lista = new ArrayList<>();
        File f = new File(ARQ_AUTOMOVEIS);
        if (!f.exists()) return lista;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                if (linha.isBlank()) continue;
                String[] p = linha.split(SEP, -1);
                if (p.length < 10) continue;
                Marca marca = new Marca(p[9]);
                Modelo modelo = new Modelo(p[8], marca);
                lista.add(new Automovel(
                        p[0],
                        Long.parseLong(p[1]),
                        p[2], p[3],
                        Integer.parseInt(p[4]),
                        Integer.parseInt(p[5]),
                        Long.parseLong(p[6]),
                        Double.parseDouble(p[7]),
                        modelo));
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler automóveis: " + e.getMessage());
        }
        return lista;
    }

    private static void escreverAutomoveis(List<Automovel> lista) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ARQ_AUTOMOVEIS, false))) {
            for (Automovel a : lista) {
                pw.println(a.getPlacaAutomovel() + SEP
                        + a.getRenavamAutomovel() + SEP
                        + a.getChassiAutomovel() + SEP
                        + a.getCorAutomovel() + SEP
                        + a.getNumeroPortasAutomovel() + SEP
                        + a.getTipoCombustivelAutomovel() + SEP
                        + a.getQuilometragemAutomovel() + SEP
                        + a.getValorLocacaoAutomovel() + SEP
                        + a.getModelo().getDescricaoModelo() + SEP
                        + a.getModelo().getMarca().getDescricao());
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar automóveis: " + e.getMessage());
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  LOCAÇÕES
    // ══════════════════════════════════════════════════════════════════════════

    public static void salvarLocacao(Locacao l) {
        List<Locacao> todas = listarLocacoes();
        todas.removeIf(x -> x.getIdLocacao() == l.getIdLocacao());
        todas.add(l);
        escreverLocacoes(todas);
    }

    public static void atualizarLocacao(Locacao l) {
        salvarLocacao(l);
    }

    public static void excluirLocacao(long id) {
        List<Locacao> todas = listarLocacoes();
        todas.removeIf(x -> x.getIdLocacao() == id);
        escreverLocacoes(todas);
    }

    public static Locacao buscarLocacao(long id) {
        return listarLocacoes().stream()
                .filter(l -> l.getIdLocacao() == id)
                .findFirst().orElse(null);
    }

    public static List<Locacao> listarLocacoes() {
        List<Locacao> lista = new ArrayList<>();
        File f = new File(ARQ_LOCACOES);
        if (!f.exists()) return lista;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                if (linha.isBlank()) continue;
                String[] p = linha.split(SEP, -1);
                if (p.length < 10) continue;

                long id = Long.parseLong(p[0]);
                LocalDate dataLoc = LocalDate.parse(p[1]);
                LocalTime horaLoc = LocalTime.parse(p[2]);
                LocalDate dataDev = p[3].isEmpty() ? null : LocalDate.parse(p[3]);
                LocalTime horaDev = p[4].isEmpty() ? null : LocalTime.parse(p[4]);
                long kmFinal = Long.parseLong(p[5]);
                double valor = Double.parseDouble(p[6]);
                Locacao.StatusLocacao status = Locacao.StatusLocacao.valueOf(p[7]);
                long cpfCliente = Long.parseLong(p[8]);
                String placa = p[9];

                Cliente cliente = buscarCliente(cpfCliente);
                Automovel automovel = buscarAutomovel(placa);

                if (cliente == null || automovel == null) continue;

                lista.add(new Locacao(id, dataLoc, horaLoc, dataDev, horaDev,
                        kmFinal, valor, status, cliente, automovel));
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler locações: " + e.getMessage());
        }
        return lista;
    }

    private static void escreverLocacoes(List<Locacao> lista) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ARQ_LOCACOES, false))) {
            for (Locacao l : lista) {
                pw.println(l.getIdLocacao() + SEP
                        + l.getDataLocacao() + SEP
                        + l.getHoraLocacao() + SEP
                        + (l.getDataDevolucao() != null ? l.getDataDevolucao() : "") + SEP
                        + (l.getHoraDevolucao() != null ? l.getHoraDevolucao() : "") + SEP
                        + l.getQuilometragemFinal() + SEP
                        + l.getValorTotal() + SEP
                        + l.getStatus() + SEP
                        + l.getCliente().getCpfCliente() + SEP
                        + l.getAutomovel().getPlacaAutomovel());
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar locações: " + e.getMessage());
        }
    }
}
