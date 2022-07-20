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
public class Acrescimo extends MateriaPrima
{

    private float valor;

    public Acrescimo(int cod, String descricao, UnidadeMedida unidadeMedida, float valor)
    {
        super(cod, descricao, unidadeMedida);
        this.valor = valor;

    }

    public Acrescimo()
    {
        super();
        this.valor = 0;
    }

    public float getValor()
    {
        return valor;
    }

    public void setValor(float valor)
    {
        this.valor = valor;
    }

}
