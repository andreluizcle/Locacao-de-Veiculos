package model;

/**
 * Representa um automóvel da frota.
 * Classe de domínio pura — sem dependências externas.
 */
public class Automovel {

    private String placa;
    private long   renavam;
    private String chassi;
    private String cor;
    private int    numeroPortas;
    private int    tipoCombustivel;   // 1-Gasolina 2-Etanol 3-Diesel 4-Flex 5-Elétrico
    private long   quilometragem;
    private double valorDiaria;
    private Modelo modelo;

    public Automovel(String placa, long renavam, String chassi, String cor,
                     int numeroPortas, int tipoCombustivel, long quilometragem,
                     double valorDiaria, Modelo modelo) {
        this.placa           = placa;
        this.renavam         = renavam;
        this.chassi          = chassi;
        this.cor             = cor;
        this.numeroPortas    = numeroPortas;
        this.tipoCombustivel = tipoCombustivel;
        this.quilometragem   = quilometragem;
        this.valorDiaria     = valorDiaria;
        this.modelo          = modelo;
    }

    // ── Getters ──────────────────────────────────────────────────────────────

    public String getPlaca()            { return placa; }
    public long   getRenavam()          { return renavam; }
    public String getChassi()           { return chassi; }
    public String getCor()              { return cor; }
    public int    getNumeroPortas()     { return numeroPortas; }
    public int    getTipoCombustivel()  { return tipoCombustivel; }
    public long   getQuilometragem()    { return quilometragem; }
    public double getValorDiaria()      { return valorDiaria; }
    public Modelo getModelo()           { return modelo; }

    // ── Setters ──────────────────────────────────────────────────────────────

    public void setPlaca(String placa)                    { this.placa = placa; }
    public void setRenavam(long renavam)                  { this.renavam = renavam; }
    public void setChassi(String chassi)                  { this.chassi = chassi; }
    public void setCor(String cor)                        { this.cor = cor; }
    public void setNumeroPortas(int numeroPortas)         { this.numeroPortas = numeroPortas; }
    public void setTipoCombustivel(int tipoCombustivel)   { this.tipoCombustivel = tipoCombustivel; }
    public void setQuilometragem(long quilometragem)      { this.quilometragem = quilometragem; }
    public void setValorDiaria(double valorDiaria)        { this.valorDiaria = valorDiaria; }
    public void setModelo(Modelo modelo)                  { this.modelo = modelo; }

    @Override
    public String toString() {
        return placa + " — " + modelo;
    }
}
