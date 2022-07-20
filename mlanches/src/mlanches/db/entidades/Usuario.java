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
public class Usuario extends Pessoa
{

    private String senha;

    public Usuario(int cod, String nome, String endereco, String telefone, float saldo, Cargo cargo, String senha)
    {
        super(cod, nome, endereco, telefone, saldo, cargo);
        this.senha = senha;
    }

    public String getSenha()
    {
        return senha;
    }

    public void setSenha(String senha)
    {
        this.senha = senha;
    }

}
