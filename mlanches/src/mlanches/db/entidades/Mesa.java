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
public class Mesa
{

    private int num;
    private LocalDateTime dataAbertura;
    private LocalDateTime dataFechamento;
    private Pessoa cliente;

    public Mesa(int num, LocalDateTime dataAbertura, LocalDateTime dataFechamento, Pessoa cliente)
    {
        this.num = num;
        this.dataAbertura = dataAbertura;
        this.dataFechamento = dataFechamento;
        this.cliente = cliente;
    }

    public Mesa()
    {
        this(0, LocalDateTime.now(), null, new Pessoa());
    }

    public int getNum()
    {
        return num;
    }

    public void setNum(int num)
    {
        this.num = num;
    }

    public LocalDateTime getDataAbertura()
    {
        return dataAbertura;
    }

    public void setDataAbertura(LocalDateTime dataAbertura)
    {
        this.dataAbertura = dataAbertura;
    }

    public LocalDateTime getDataFechamento()
    {
        return dataFechamento;
    }

    public void setDataFechamento(LocalDateTime dataFechamento)
    {
        this.dataFechamento = dataFechamento;
    }

    public Pessoa getCliente()
    {
        return cliente;
    }

    public void setCliente(Pessoa cliente)
    {
        this.cliente = cliente;
    }

    @Override
    public String toString()
    {
        return (this.num == 0 ? "Tipo:Entrega" : "Mesa:" + this.num) + " - Cliente:" + this.cliente.getNome();
    }
}
