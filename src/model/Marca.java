package model;

/**
 * Representa a marca de um automóvel (ex.: Toyota, Honda).
 * Classe de domínio pura — sem dependências externas.
 */
public class Marca {

    private String descricao;

    public Marca(String descricao) {
        if (descricao == null || descricao.isBlank())
            throw new IllegalArgumentException("Descrição da marca não pode ser vazia.");
        this.descricao = descricao.trim();
    }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    @Override
    public String toString() { return descricao; }
}
