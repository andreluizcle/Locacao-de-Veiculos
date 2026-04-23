package model;

/**
 * Representa um cliente da locadora.
 * Classe de domínio pura — sem dependências externas.
 */
public class Cliente {

    private long   cpf;
    private String nome;
    private String endereco;
    private String telefone;
    private String email;

    public Cliente(long cpf, String nome, String endereco, String telefone, String email) {
        this.cpf      = cpf;
        this.nome     = nome;
        this.endereco = endereco;
        this.telefone = telefone;
        this.email    = email;
    }

    // ── Getters ──────────────────────────────────────────────────────────────

    public long   getCpf()      { return cpf; }
    public String getNome()     { return nome; }
    public String getEndereco() { return endereco; }
    public String getTelefone() { return telefone; }
    public String getEmail()    { return email; }

    // ── Setters ──────────────────────────────────────────────────────────────

    public void setNome(String nome)         { this.nome = nome; }
    public void setEndereco(String endereco) { this.endereco = endereco; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public void setEmail(String email)       { this.email = email; }

    @Override
    public String toString() {
        return nome + " (CPF: " + cpf + ")";
    }
}
