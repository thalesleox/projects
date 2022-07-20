/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rosolen.db.entidades;

import java.sql.Timestamp;

/**
 *
 * @author thale
 */
public class tipoLancamento
{

    private int cod;
    private Timestamp data;

    public tipoLancamento(int cod, Timestamp data)
    {
        this.cod = cod;
        this.data = data;
    }

    public int getCod()
    {
        return cod;
    }

    public void setCod(int cod)
    {
        this.cod = cod;
    }

    public Timestamp getData()
    {
        return data;
    }

    public void setData(Timestamp data)
    {
        this.data = data;
    }

}
