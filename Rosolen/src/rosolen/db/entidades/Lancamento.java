/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rosolen.db.entidades;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 *
 * @author thale
 */
public class Lancamento
{

    private int cod;
    private Pessoa cliente;
    private Pessoa academia;
    private Timestamp dataMens;
    private Timestamp dataPagamento;
    private boolean avaliacao;
    private boolean mensalidade;
    private String descricao;
    private float total;

    public Lancamento(int cod, Pessoa cliente, Pessoa academia, Timestamp dataMens, Timestamp dataPagamento, boolean avaliacao, boolean mensalidade, String descricao, float total)
    {
        this.cod = cod;
        this.cliente = cliente;
        this.academia = academia;
        this.dataMens = dataMens;
        this.dataPagamento = dataPagamento;
        this.avaliacao = avaliacao;
        this.mensalidade = mensalidade;
        this.descricao = descricao;
        this.total = total;
    }

    public Lancamento(Pessoa cliente, Pessoa academia, Timestamp dataMens, Timestamp dataPagamento, boolean avaliacao, boolean mensalidade, String descricao, float total)
    {
        this(0, cliente, academia, dataMens, dataPagamento, avaliacao, mensalidade, descricao, total);
    }

    public Lancamento()
    {
        this(0, new Pessoa(), new Pessoa(), Timestamp.valueOf(LocalDateTime.now()), Timestamp.valueOf(LocalDateTime.now()), false, false, "", 0f);
    }

    public int getCod()
    {
        return cod;
    }

    public void setCod(int cod)
    {
        this.cod = cod;
    }

    public Pessoa getCliente()
    {
        return cliente;
    }

    public void setCliente(Pessoa cliente)
    {
        this.cliente = cliente;
    }

    public Pessoa getAcademia()
    {
        return academia;
    }

    public void setAcademia(Pessoa academia)
    {
        this.academia = academia;
    }

    public Timestamp getDataMens()
    {
        return dataMens;
    }

    public void setDataMens(Timestamp dataMens)
    {
        this.dataMens = dataMens;
    }

    public Timestamp getDataPagamento()
    {
        return dataPagamento;
    }

    public void setDataPagamento(Timestamp dataPagamento)
    {
        this.dataPagamento = dataPagamento;
    }

    public boolean isAvaliacao()
    {
        return avaliacao;
    }

    public void setAvaliacao(boolean avaliacao)
    {
        this.avaliacao = avaliacao;
    }

    public boolean isMensalidade()
    {
        return mensalidade;
    }

    public void setMensalidade(boolean mensalidade)
    {
        this.mensalidade = mensalidade;
    }

    public String getDescricao()
    {
        return descricao;
    }

    public void setDescricao(String descricao)
    {
        this.descricao = descricao;
    }

    public float getTotal()
    {
        return total;
    }

    public void setTotal(float total)
    {
        this.total = total;
    }

}
