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
public class Servico
{

    private int cod;
    private String descricao;
    private float valor;

    public Servico(int cod, String descricao, float valor)
    {
        this.cod = cod;
        this.descricao = descricao;
        this.valor = valor;
    }

    public Servico()
    {
        this(0, "", 0);
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
        return descricao;
    }

    public void setDescricao(String descricao)
    {
        this.descricao = descricao;
    }

    public float getValor()
    {
        return valor;
    }

    public void setValor(float valor)
    {
        this.valor = valor;
    }

    @Override
    public String toString()
    {
        return this.descricao;
    }

}
