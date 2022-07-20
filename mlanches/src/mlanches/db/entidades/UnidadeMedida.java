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
public class UnidadeMedida
{

    private int cod;
    private String descricao;
    private String sigla;

    public UnidadeMedida(int cod, String descricao, String sigla)
    {
        this.cod = cod;
        this.descricao = descricao;
        this.sigla = sigla;
    }

    public UnidadeMedida(String descricao, String sigla)
    {
        this.cod = 0;
        this.descricao = descricao;
        this.sigla = sigla;
    }

    public UnidadeMedida()
    {
        this(0, "", "");
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

    public String getSigla()
    {
        return sigla;
    }

    public void setSigla(String sigla)
    {
        this.sigla = sigla;
    }

    @Override
    public String toString()
    {
        return this.descricao + "(s) - " + this.sigla;
    }
}
