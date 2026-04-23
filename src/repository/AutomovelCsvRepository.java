package repository;

import model.Automovel;
import model.Marca;
import model.Modelo;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Persiste automóveis em um arquivo CSV.
 * Formato: placa;renavam;chassi;cor;portas;combustivel;km;valorDiaria;descModelo;descMarca
 */
public class AutomovelCsvRepository implements AutomovelRepository {

    private static final String SEP = ";";
    private final File arquivo;

    public AutomovelCsvRepository(String caminho) {
        this.arquivo = new File(caminho);
        arquivo.getParentFile().mkdirs();
    }

    @Override
    public void salvar(Automovel automovel) {
        List<Automovel> lista = listarTodos();
        lista.removeIf(a -> a.getPlaca().equalsIgnoreCase(automovel.getPlaca()));
        lista.add(automovel);
        escrever(lista);
    }

    @Override
    public void excluir(String placa) {
        List<Automovel> lista = listarTodos();
        lista.removeIf(a -> a.getPlaca().equalsIgnoreCase(placa));
        escrever(lista);
    }

    @Override
    public Optional<Automovel> buscarPorPlaca(String placa) {
        return listarTodos().stream()
                .filter(a -> a.getPlaca().equalsIgnoreCase(placa))
                .findFirst();
    }

    @Override
    public List<Automovel> listarTodos() {
        List<Automovel> lista = new ArrayList<>();
        if (!arquivo.exists()) return lista;

        try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                if (linha.isBlank()) continue;
                String[] p = linha.split(SEP, -1);
                if (p.length < 10) continue;
                Marca  marca  = new Marca(p[9]);
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
            System.err.println("[AutomovelCsvRepository] Erro ao ler: " + e.getMessage());
        }
        return lista;
    }

    private void escrever(List<Automovel> lista) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(arquivo, false))) {
            for (Automovel a : lista) {
                pw.println(a.getPlaca() + SEP
                        + a.getRenavam() + SEP
                        + a.getChassi() + SEP
                        + a.getCor() + SEP
                        + a.getNumeroPortas() + SEP
                        + a.getTipoCombustivel() + SEP
                        + a.getQuilometragem() + SEP
                        + a.getValorDiaria() + SEP
                        + a.getModelo().getDescricao() + SEP
                        + a.getModelo().getMarca().getDescricao());
            }
        } catch (IOException e) {
            System.err.println("[AutomovelCsvRepository] Erro ao salvar: " + e.getMessage());
        }
    }
}
