/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rosolen.db.entidades;

/**
 *
 * @author thale
 */
public class Situacao
{

    private int cod;
    private String Descricao;

    public Situacao()
    {
        this.cod = -1;
        this.Descricao = "";
    }

    public Situacao(int cod, String Descricao)
    {
        this.cod = cod;
        this.Descricao = Descricao;
    }

    public int getCod()
    {
        return cod;
    }

    public void setCod(int cod)
    {
        this.cod = cod;
    }

    public String getDescricao()
    {
        return Descricao;
    }

    public void setDescricao(String Descricao)
    {
        this.Descricao = Descricao;
    }

    @Override
    public String toString()
    {
        return this.Descricao; //To change body of generated methods, choose Tools | Templates.
    }
}
