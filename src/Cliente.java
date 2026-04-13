import java.util.ArrayList;
import java.util.List;

public class Cliente {
    private long cpfCliente;
    private String nomeCliente;
    private String enderecoCliente;
    private String telefoneCliente;
    private String emailCliente;
    private List<Locacao> locacoes = new ArrayList<>();

    public Cliente(long cpfCliente, String nomeCliente, String enderecoCliente,
                   String telefoneCliente, String emailCliente) {
        this.cpfCliente = cpfCliente;
        this.nomeCliente = nomeCliente;
        this.enderecoCliente = enderecoCliente;
        this.telefoneCliente = telefoneCliente;
        this.emailCliente = emailCliente;
    }

    public String consultarCliente() {
        return this.toString();
    }

    // ── Getters ──────────────────────────────────────────────────────────────

    public long getCpfCliente() { return cpfCliente; }
    public String getNomeCliente() { return nomeCliente; }
    public String getEnderecoCliente() { return enderecoCliente; }
    public String getTelefoneCliente() { return telefoneCliente; }
    public String getEmailCliente() { return emailCliente; }

    // ── Setters ──────────────────────────────────────────────────────────────

    public void setNomeCliente(String nomeCliente) { this.nomeCliente = nomeCliente; }
    public void setEnderecoCliente(String enderecoCliente) { this.enderecoCliente = enderecoCliente; }
    public void setTelefoneCliente(String telefoneCliente) { this.telefoneCliente = telefoneCliente; }
    public void setEmailCliente(String emailCliente) { this.emailCliente = emailCliente; }

    @Override
    public String toString() {
        return "Cliente{" + "nome='" + nomeCliente + '\'' + ", cpf=" + cpfCliente + '}';
    }
}