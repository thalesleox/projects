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
public class Recebimento
{

    private LocalDateTime datapagamento;
    private float valor_pago;
    private float troco;
    private int numero;
    private boolean pagou;
    private boolean marcou;
    private boolean dinheiro;
    private boolean cartao;

    public Recebimento(LocalDateTime datapagamento, float valor_pago, float troco, int numero, boolean pagou, boolean marcou, boolean dinheiro, boolean cartao)
    {
        this.datapagamento = datapagamento;
        this.valor_pago = valor_pago;
        this.troco = troco;
        this.numero = numero;
        this.pagou = pagou;
        this.marcou = marcou;
        this.dinheiro = dinheiro;
        this.cartao = cartao;
    }

    public LocalDateTime getDatapagamento()
    {
        return datapagamento;
    }

    public void setDatapagamento(LocalDateTime datapagamento)
    {
        this.datapagamento = datapagamento;
    }

    public float getValor_pago()
    {
        return valor_pago;
    }

    public void setValor_pago(float valor_pago)
    {
        this.valor_pago = valor_pago;
    }

    public float getTroco()
    {
        return troco;
    }

    public void setTroco(float troco)
    {
        this.troco = troco;
    }

    public int getNumero()
    {
        return numero;
    }

    public void setNumero(int numero)
    {
        this.numero = numero;
    }

    public boolean isPagou()
    {
        return pagou;
    }

    public void setPagou(boolean pagou)
    {
        this.pagou = pagou;
    }

    public boolean isMarcou()
    {
        return marcou;
    }

    public void setMarcou(boolean marcou)
    {
        this.marcou = marcou;
    }

    public boolean isDinheiro()
    {
        return dinheiro;
    }

    public void setDinheiro(boolean dinheiro)
    {
        this.dinheiro = dinheiro;
    }

    public boolean isCartao()
    {
        return cartao;
    }

    public void setCartao(boolean cartao)
    {
        this.cartao = cartao;
    }

}
