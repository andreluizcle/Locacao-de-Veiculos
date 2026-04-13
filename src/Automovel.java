import java.util.ArrayList;
import java.util.List;

public class Automovel {
    private String placaAutomovel;
    private long renavamAutomovel;
    private String chassiAutomovel;
    private String corAutomovel;
    private int numeroPortasAutomovel;
    private int tipoCombustivelAutomovel;
    private long quilometragemAutomovel;
    private double valorLocacaoAutomovel;

    private Modelo modelo;
    private List<Locacao> locacoes = new ArrayList<>();

    public Automovel(String placaAutomovel, long renavamAutomovel, String chassiAutomovel,
                     String corAutomovel, int numeroPortasAutomovel, int tipoCombustivelAutomovel,
                     long quilometragemAutomovel, double valorLocacaoAutomovel, Modelo modelo) {
        this.placaAutomovel = placaAutomovel;
        this.renavamAutomovel = renavamAutomovel;
        this.chassiAutomovel = chassiAutomovel;
        this.corAutomovel = corAutomovel;
        this.numeroPortasAutomovel = numeroPortasAutomovel;
        this.tipoCombustivelAutomovel = tipoCombustivelAutomovel;
        this.quilometragemAutomovel = quilometragemAutomovel;
        this.valorLocacaoAutomovel = valorLocacaoAutomovel;
        this.modelo = modelo;
    }

    // ── Getters ──────────────────────────────────────────────────────────────

    public String getPlacaAutomovel() { return placaAutomovel; }
    public long getRenavamAutomovel() { return renavamAutomovel; }
    public String getChassiAutomovel() { return chassiAutomovel; }
    public String getCorAutomovel() { return corAutomovel; }
    public int getNumeroPortasAutomovel() { return numeroPortasAutomovel; }
    public int getTipoCombustivelAutomovel() { return tipoCombustivelAutomovel; }
    public long getQuilometragemAutomovel() { return quilometragemAutomovel; }
    public double getValorLocacaoAutomovel() { return valorLocacaoAutomovel; }
    public Modelo getModelo() { return modelo; }

    // ── Setters ──────────────────────────────────────────────────────────────

    public void setPlacaAutomovel(String placaAutomovel) { this.placaAutomovel = placaAutomovel; }
    public void setRenavamAutomovel(long renavamAutomovel) { this.renavamAutomovel = renavamAutomovel; }
    public void setChassiAutomovel(String chassiAutomovel) { this.chassiAutomovel = chassiAutomovel; }
    public void setCorAutomovel(String corAutomovel) { this.corAutomovel = corAutomovel; }
    public void setNumeroPortasAutomovel(int numeroPortasAutomovel) { this.numeroPortasAutomovel = numeroPortasAutomovel; }
    public void setTipoCombustivelAutomovel(int tipoCombustivelAutomovel) { this.tipoCombustivelAutomovel = tipoCombustivelAutomovel; }
    public void setQuilometragemAutomovel(long quilometragemAutomovel) { this.quilometragemAutomovel = quilometragemAutomovel; }
    public void setValorLocacaoAutomovel(double valorLocacaoAutomovel) { this.valorLocacaoAutomovel = valorLocacaoAutomovel; }
    public void setModelo(Modelo modelo) { this.modelo = modelo; }

    public String consultarAutomovel() { return this.toString(); }

    @Override
    public String toString() {
        return "Automovel{placa='" + placaAutomovel + "', modelo=" + modelo + '}';
    }
}