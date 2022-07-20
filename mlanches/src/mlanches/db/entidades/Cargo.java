/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mlanches.db.entidades;

/**
 *
 * @author thale
 */
public class Cargo
{

    private int cod;
    private String descricao;

    public Cargo(int cod, String descricao)
    {
        this.cod = cod;
        this.descricao = descricao;
    }

    public Cargo()
    {
        this.cod = 2;
        this.descricao = "Cliente";
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

    @Override
    public String toString()
    {
        return this.cod + " - " + this.descricao;
    }

}
