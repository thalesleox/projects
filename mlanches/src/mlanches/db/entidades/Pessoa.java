/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mlanches.db.entidades;

/**
 *
 * @author Aluno
 */
public class Pessoa
{

    private int cod;
    private String nome;
    private String endereco;
    private String telefone;
    private float saldo;
    private Cargo cargo;

    public Pessoa(int cod, String nome, String endereco, String telefone, float saldo, Cargo cargo)
    {
        this.cod = cod;
        this.nome = nome;
        this.endereco = endereco;
        this.telefone = telefone;
        this.saldo = saldo;
        this.cargo = cargo;
    }

    public Pessoa(String nome, String endereco, String telefone, float saldo, Cargo cargo)
    {
        this.cod = 0;
        this.nome = nome;
        this.endereco = endereco;
        this.telefone = telefone;
        this.saldo = saldo;
        this.cargo = cargo;
    }

    public Pessoa()
    {
        this(0, "", "", "", 0, new Cargo());
    }

    public int getCod()
    {
        return cod;
    }

    public void setCod(int cod)
    {
        this.cod = cod;
    }

    public String getNome()
    {
        return nome;
    }

    public void setNome(String nome)
    {
        this.nome = nome;
    }

    public String getEndereco()
    {
        return endereco;
    }

    public void setEndereco(String endereco)
    {
        this.endereco = endereco;
    }

    public String getTelefone()
    {
        return telefone;
    }

    public void setTelefone(String telefone)
    {
        this.telefone = telefone;
    }

    public float getSaldo()
    {
        return saldo;
    }

    public void setSaldo(float saldo)
    {
        this.saldo = saldo;
    }

    public Cargo getCargo()
    {
        return cargo;
    }

    public void setCargo(Cargo cargo)
    {
        this.cargo = cargo;
    }
}
