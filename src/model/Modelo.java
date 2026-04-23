package model;

/**
 * Representa o modelo de um automóvel (ex.: Corolla, Civic).
 * Classe de domínio pura — sem dependências externas.
 */
public class Modelo {

    private String descricao;
    private Marca  marca;

    public Modelo(String descricao, Marca marca) {
        if (descricao == null || descricao.isBlank())
            throw new IllegalArgumentException("Descrição do modelo não pode ser vazia.");
        if (marca == null)
            throw new IllegalArgumentException("Marca é obrigatória.");
        this.descricao = descricao.trim();
        this.marca     = marca;
    }

    public String getDescricao() { return descricao; }
    public Marca  getMarca()     { return marca; }

    public void setDescricao(String descricao) { this.descricao = descricao; }
    public void setMarca(Marca marca)           { this.marca = marca; }

    @Override
    public String toString() {
        return descricao + " (" + marca.getDescricao() + ")";
    }
}
