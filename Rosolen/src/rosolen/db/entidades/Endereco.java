package rosolen.db.entidades;

public class Endereco
{

    private int cod;
    private String cep;
    private String tipolog;
    private String logradouro;
    private String bairro;
    private int numero;

    public Endereco(int cod, String cep, String tipolog, String logradouro, String bairro, int numero)
    {
        this.cod = cod;
        this.cep = cep;
        this.tipolog = tipolog;
        this.logradouro = logradouro;
        this.bairro = bairro;
        this.numero = numero;
    }

    public Endereco()
    {
        this(0, "", "", "", "", 0);
    }

    public int getCod()
    {
        return cod;
    }

    public void setCod(int cod)
    {
        this.cod = cod;
    }

    public String getCep()
    {
        return cep;
    }

    public void setCep(String cep)
    {
        this.cep = cep;
    }

    public String getTipolog()
    {
        return tipolog;
    }

    public void setTipolog(String tipolog)
    {
        this.tipolog = tipolog;
    }

    public String getLogradouro()
    {
        return logradouro;
    }

    public void setLogradouro(String logradouro)
    {
        this.logradouro = logradouro;
    }

    public String getBairro()
    {
        return bairro;
    }

    public void setBairro(String bairro)
    {
        this.bairro = bairro;
    }

    public int getNumero()
    {
        return numero;
    }

    public void setNumero(int numero)
    {
        this.numero = numero;
    }

    @Override
    public String toString()
    {
        return this.cep;
    }

}
