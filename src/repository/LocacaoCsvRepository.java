package repository;

import model.Automovel;
import model.Cliente;
import model.Locacao;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Persiste locações em um arquivo CSV.
 * Formato: id;dataLoc;horaLoc;dataDev;horaDev;kmFinal;valorTotal;status;cpfCliente;placaAutomovel
 *
 * Depende dos outros repositórios apenas para resolver as referências ao reidratarmos
 * as entidades associadas (Cliente e Automovel) ao ler do arquivo.
 */
public class LocacaoCsvRepository implements LocacaoRepository {

    private static final String SEP = ";";

    private final File               arquivo;
    private final ClienteRepository  clienteRepo;
    private final AutomovelRepository automovelRepo;

    public LocacaoCsvRepository(String caminho,
                                 ClienteRepository clienteRepo,
                                 AutomovelRepository automovelRepo) {
        this.arquivo       = new File(caminho);
        this.clienteRepo   = clienteRepo;
        this.automovelRepo = automovelRepo;
        arquivo.getParentFile().mkdirs();
    }

    @Override
    public void salvar(Locacao locacao) {
        List<Locacao> lista = listarTodas();
        lista.removeIf(l -> l.getId() == locacao.getId());
        lista.add(locacao);
        escrever(lista);
    }

    @Override
    public void excluir(long id) {
        List<Locacao> lista = listarTodas();
        lista.removeIf(l -> l.getId() == id);
        escrever(lista);
    }

    @Override
    public Optional<Locacao> buscarPorId(long id) {
        return listarTodas().stream()
                .filter(l -> l.getId() == id)
                .findFirst();
    }

    @Override
    public List<Locacao> listarTodas() {
        List<Locacao> lista = new ArrayList<>();
        if (!arquivo.exists()) return lista;

        try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                if (linha.isBlank()) continue;
                String[] p = linha.split(SEP, -1);
                if (p.length < 10) continue;

                long      id       = Long.parseLong(p[0]);
                LocalDate dataLoc  = LocalDate.parse(p[1]);
                LocalTime horaLoc  = LocalTime.parse(p[2]);
                LocalDate dataDev  = p[3].isEmpty() ? null : LocalDate.parse(p[3]);
                LocalTime horaDev  = p[4].isEmpty() ? null : LocalTime.parse(p[4]);
                long      kmFinal  = Long.parseLong(p[5]);
                double    valor    = Double.parseDouble(p[6]);
                Locacao.Status status = Locacao.Status.valueOf(p[7]);
                long      cpf      = Long.parseLong(p[8]);
                String    placa    = p[9];

                Optional<Cliente>   cli  = clienteRepo.buscarPorCpf(cpf);
                Optional<Automovel> auto = automovelRepo.buscarPorPlaca(placa);

                if (cli.isEmpty() || auto.isEmpty()) {
                    System.err.println("[LocacaoCsvRepository] Referência inválida na linha: " + linha);
                    continue;
                }

                Locacao l = new Locacao(id, dataLoc, horaLoc, dataDev, horaDev,
                        kmFinal, valor, status, cli.get(), auto.get());
                lista.add(l);
            }
        } catch (IOException e) {
            System.err.println("[LocacaoCsvRepository] Erro ao ler: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public List<Locacao> listarAtivas() {
        return listarTodas().stream()
                .filter(Locacao::isAtiva)
                .collect(Collectors.toList());
    }

    private void escrever(List<Locacao> lista) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(arquivo, false))) {
            for (Locacao l : lista) {
                pw.println(l.getId() + SEP
                        + l.getDataLocacao() + SEP
                        + l.getHoraLocacao() + SEP
                        + (l.getDataDevolucao() != null ? l.getDataDevolucao() : "") + SEP
                        + (l.getHoraDevolucao() != null ? l.getHoraDevolucao() : "") + SEP
                        + l.getQuilometragemFinal() + SEP
                        + l.getValorTotal() + SEP
                        + l.getStatus() + SEP
                        + l.getCliente().getCpf() + SEP
                        + l.getAutomovel().getPlaca());
            }
        } catch (IOException e) {
            System.err.println("[LocacaoCsvRepository] Erro ao salvar: " + e.getMessage());
        }
    }
}
