import java.util.ArrayList;
import java.util.List;

public class Marca {
    private String descricao;
    private List<Modelo> modelos = new ArrayList<>();

    public Marca(String descricao) {
        this.descricao = descricao;
    }

    public String comMarca() { return this.toString(); }
    public String getDescricao() { return descricao; }

    @Override
    public String toString() {
        return descricao;
    }
}