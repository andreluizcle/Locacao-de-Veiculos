import java.util.ArrayList;
import java.util.List;

public class Modelo {
    private String descricaoModelo;
    private Marca marca;
    private List<Automovel> automoveis = new ArrayList<>();

    public Modelo(String descricaoModelo, Marca marca) {
        this.descricaoModelo = descricaoModelo;
        this.marca = marca;
    }

    public String consultarModelo() { return this.toString(); }

    public String getDescricaoModelo() { return descricaoModelo; }
    public Marca getMarca() { return marca; }

    @Override
    public String toString() {
        return descricaoModelo + " (" + (marca != null ? marca.getDescricao() : "?") + ")";
    }
}