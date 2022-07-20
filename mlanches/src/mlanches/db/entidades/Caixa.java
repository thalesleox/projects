/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mlanches.db.entidades;

import java.time.LocalDateTime;

/**
 *
 * @author thale
 */
public class Caixa
{

    private int cod;
    private String status;
    private LocalDateTime data;
    private float saldoInicial;
    private float saldoFinal;
    private Pessoa funcionario;

    public Caixa(int cod, String status, LocalDateTime data, float saldoInicial, float saldoFinal, Pessoa funcionario)
    {
        this.cod = cod;
        this.status = status;
        this.data = data;
        this.saldoInicial = saldoInicial;
        this.saldoFinal = saldoFinal;
        this.funcionario = funcionario;
    }

    public int getCod()
    {
        return cod;
    }

    public void setCod(int cod)
    {
        this.cod = cod;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public LocalDateTime getData()
    {
        return data;
    }

    public void setData(LocalDateTime data)
    {
        this.data = data;
    }

    public float getSaldoInicial()
    {
        return saldoInicial;
    }

    public void setSaldoInicial(float saldoInicial)
    {
        this.saldoInicial = saldoInicial;
    }

    public float getSaldoFinal()
    {
        return saldoFinal;
    }

    public void setSaldoFinal(float saldoFinal)
    {
        this.saldoFinal = saldoFinal;
    }

    public Pessoa getFuncionario()
    {
        return funcionario;
    }

    public void setFuncionario(Pessoa funcionario)
    {
        this.funcionario = funcionario;
    }

}
