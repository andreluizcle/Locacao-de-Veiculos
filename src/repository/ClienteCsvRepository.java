package repository;

import model.Cliente;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Persiste clientes em um arquivo CSV.
 * Formato: cpf;nome;endereco;telefone;email
 */
public class ClienteCsvRepository implements ClienteRepository {

    private static final String SEP = ";";
    private final File arquivo;

    public ClienteCsvRepository(String caminho) {
        this.arquivo = new File(caminho);
        arquivo.getParentFile().mkdirs();
    }

    @Override
    public void salvar(Cliente cliente) {
        List<Cliente> lista = listarTodos();
        lista.removeIf(c -> c.getCpf() == cliente.getCpf()); // upsert por CPF
        lista.add(cliente);
        escrever(lista);
    }

    @Override
    public void excluir(long cpf) {
        List<Cliente> lista = listarTodos();
        lista.removeIf(c -> c.getCpf() == cpf);
        escrever(lista);
    }

    @Override
    public Optional<Cliente> buscarPorCpf(long cpf) {
        return listarTodos().stream()
                .filter(c -> c.getCpf() == cpf)
                .findFirst();
    }

    @Override
    public List<Cliente> listarTodos() {
        List<Cliente> lista = new ArrayList<>();
        if (!arquivo.exists()) return lista;

        try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                if (linha.isBlank()) continue;
                String[] p = linha.split(SEP, -1);
                if (p.length < 5) continue;
                lista.add(new Cliente(
                        Long.parseLong(p[0]), p[1], p[2], p[3], p[4]));
            }
        } catch (IOException e) {
            System.err.println("[ClienteCsvRepository] Erro ao ler: " + e.getMessage());
        }
        return lista;
    }

    private void escrever(List<Cliente> lista) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(arquivo, false))) {
            for (Cliente c : lista) {
                pw.println(c.getCpf() + SEP + c.getNome() + SEP
                        + c.getEndereco() + SEP + c.getTelefone() + SEP + c.getEmail());
            }
        } catch (IOException e) {
            System.err.println("[ClienteCsvRepository] Erro ao salvar: " + e.getMessage());
        }
    }
}
