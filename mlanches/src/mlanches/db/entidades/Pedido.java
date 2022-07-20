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
public class Pedido
{

    private int cod;
    private Pessoa cliente;
    private Pessoa funcionario;
    private LocalDateTime horarioRealizado;
    private LocalDateTime horarioSaida;
    private LocalDateTime horariochegada;
    private String descricao;
    private String observacao;
    private float total;
    private float troco;
    private String tipoPedido;
    private boolean cancelado;
    private int mesa;
    private boolean pago;

    public Pedido(int cod, Pessoa cliente, Pessoa funcionario, LocalDateTime horarioRealizado, LocalDateTime horarioSaida, LocalDateTime horariochegada, String descricao, String observacao,
            float total, float troco, String tipoPedido, boolean cancelado, int mesa, boolean pago)
    {
        this.cod = cod;
        this.cliente = cliente;
        this.funcionario = funcionario;
        this.horarioRealizado = horarioRealizado;
        this.horarioSaida = horarioSaida;
        this.horariochegada = horariochegada;
        this.descricao = descricao;
        this.observacao = observacao;
        this.total = total;
        this.troco = troco;
        this.tipoPedido = tipoPedido;
        this.cancelado = cancelado;
        this.mesa = mesa;
        this.pago = pago;
    }

    public Pedido(Pessoa cliente, Pessoa funcionario, LocalDateTime horarioRealizado, LocalDateTime horarioSaida, LocalDateTime horariochegada, String descricao, String observacao,
            float total, float troco, String tipoPedido, boolean cancelado, int mesa, boolean pago)
    {
        this.cliente = cliente;
        this.funcionario = funcionario;
        this.horarioRealizado = horarioRealizado;
        this.horarioSaida = horarioSaida;
        this.horariochegada = horariochegada;
        this.descricao = descricao;
        this.observacao = observacao;
        this.total = total;
        this.troco = troco;
        this.tipoPedido = tipoPedido;
        this.cancelado = cancelado;
        this.mesa = mesa;
        this.pago = pago;
    }

    public Pedido()
    {
        this(0, new Pessoa(), new Pessoa(), LocalDateTime.now(), null, null, "", "", 0, 0, "NaoDefinido", Boolean.FALSE, 0, false);
    }

    public int getCod()
    {
        return cod;
    }

    public void setCod(int cod)
    {
        this.cod = cod;
    }

    public Pessoa getPessoa()
    {
        return cliente;
    }

    public void setPessoa(Pessoa cliente)
    {
        this.cliente = cliente;
    }

    public Pessoa getFuncionario()
    {
        return funcionario;
    }

    public void setFuncionario(Pessoa funcionario)
    {
        this.funcionario = funcionario;
    }

    public LocalDateTime getHorarioRealizado()
    {
        return horarioRealizado;
    }

    public void setHorarioRealizado(LocalDateTime horarioRealizado)
    {
        this.horarioRealizado = horarioRealizado;
    }

    public LocalDateTime getHorarioSaida()
    {
        return horarioSaida;
    }

    public void setHorarioSaida(LocalDateTime horarioSaida)
    {
        this.horarioSaida = horarioSaida;
    }

    public LocalDateTime getHorariochegada()
    {
        return horariochegada;
    }

    public void setHorariochegada(LocalDateTime horariochegada)
    {
        this.horariochegada = horariochegada;
    }

    public String getDescricao()
    {
        return descricao;
    }

    public void setDescricao(String descricao)
    {
        this.descricao = descricao;
    }

    public String getObservacao()
    {
        return observacao;
    }

    public void setObservacao(String observacao)
    {
        this.observacao = observacao;
    }

    public float getTotal()
    {
        return total;
    }

    public void setTotal(float total)
    {
        this.total = total;
    }

    public float getTroco()
    {
        return troco;
    }

    public void setTroco(float troco)
    {
        this.troco = troco;
    }

    public String getTipoPedido()
    {
        return tipoPedido;
    }

    public void setTipoPedido(String tipoPedido)
    {
        this.tipoPedido = tipoPedido;
    }

    public boolean isCancelado()
    {
        return cancelado;
    }

    public void setCancelado(boolean cancelado)
    {
        this.cancelado = cancelado;
    }

    public Pessoa getCliente()
    {
        return cliente;
    }

    public void setCliente(Pessoa cliente)
    {
        this.cliente = cliente;
    }

    public int getMesa()
    {
        return mesa;
    }

    public void setMesa(int mesa)
    {
        this.mesa = mesa;
    }

    public boolean isPago()
    {
        return pago;
    }

    public void setPago(boolean pago)
    {
        this.pago = pago;
    }

    @Override
    public String toString()
    {
        return "Mesa:" + this.mesa + " / Cliente:" + this.cliente.getNome() + " / " + this.descricao;
    }

}
