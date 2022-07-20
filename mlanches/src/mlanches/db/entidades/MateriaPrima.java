/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mlanches.db.entidades;

import mlanches.db.entidades.UnidadeMedida;

/**
 *
 * @author thale
 */
public class MateriaPrima
{

    private int cod;
    private String descricao;
    private UnidadeMedida unidadeMedida;

    public MateriaPrima(int cod, String descricao, UnidadeMedida unidadeMedida)
    {
        this.cod = cod;
        this.descricao = descricao;
        this.unidadeMedida = unidadeMedida;
    }

    public MateriaPrima(String descricao, UnidadeMedida unidadeMedida)
    {
        this.cod = 0;
        this.descricao = descricao;
        this.unidadeMedida = unidadeMedida;
    }

    public MateriaPrima()
    {
        this(0, "", new UnidadeMedida());
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

    public UnidadeMedida getUnidadeMedida()
    {
        return unidadeMedida;
    }

    public void setUnidadeMedida(UnidadeMedida unidadeMedida)
    {
        this.unidadeMedida = unidadeMedida;
    }

    @Override
    public String toString()
    {
        return this.cod + " - " + this.descricao; //To change body of generated methods, choose Tools | Templates.
    }

}
