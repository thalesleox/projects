/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mlanches.db.entidades;

import java.util.ArrayList;

/**
 *
 * @author thale
 */
public class Produto
{

    private int cod;
    private String descricao;
    private UnidadeMedida unidadeMedida;
    private float valor;
    private ArrayList<MateriaPrima> ingredientes;
    private String tag;

    public Produto(int cod, String descricao, UnidadeMedida unidadeMedida, float valor, ArrayList<MateriaPrima> ingredientes, String tag)
    {
        this.cod = cod;
        this.descricao = descricao;
        this.unidadeMedida = unidadeMedida;
        this.valor = valor;
        this.ingredientes = ingredientes;
        this.tag = tag;
    }

    public Produto(String descricao, UnidadeMedida unidadeMedida, float valor, ArrayList<MateriaPrima> ingredientes, String tag)
    {
        this.cod = 0;
        this.descricao = descricao;
        this.unidadeMedida = unidadeMedida;
        this.valor = valor;
        this.ingredientes = ingredientes;
        this.tag = tag;
    }

    public Produto()
    {
        this(0, "", new UnidadeMedida(), 0, null, "ou");
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

    public float getValor()
    {
        return valor;
    }

    public void setValor(float valor)
    {
        this.valor = valor;
    }

    public ArrayList<MateriaPrima> getIngredientes()
    {
        return ingredientes;
    }

    public void setIngredientes(ArrayList<MateriaPrima> ingredientes)
    {
        this.ingredientes = ingredientes;
    }

    public String getTag()
    {
        return tag;
    }

    public void setTag(String tag)
    {
        this.tag = tag;
    }

}
