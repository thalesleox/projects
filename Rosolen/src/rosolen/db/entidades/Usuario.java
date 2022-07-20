/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rosolen.db.entidades;

import java.awt.image.BufferedImage;
import java.sql.Date;
import java.sql.Timestamp;

/**
 *
 * @author thale
 */
public class Usuario extends Pessoa
{

    private String senha;
    private Categoria categoria;

    public Usuario(int cod, String nome, String cpf, String telefone, String email, Endereco endereco,
            BufferedImage img, char sexo, Situacao situacao, Timestamp datacadastro, Timestamp datanascimento, float credito, int mesvip, int familia, String senha, Categoria categoria)
    {
        super(cod, nome, cpf, telefone, email, endereco, img, sexo, situacao, datacadastro, datanascimento, credito, mesvip, familia);
        this.senha = senha;
        this.categoria = categoria;
    }

    public Usuario()
    {
        super(0, "", "", "", "", null, null, 'i', null, null, null, 0, 0, 0);
        this.senha = "";
        this.categoria = new Categoria();
    }

    public Usuario(String nome, String cpf, String telefone, String email, Endereco endereco,
            BufferedImage img, char sexo, Situacao situacao, Timestamp datacadastro, Timestamp datanascimento, float credito, int mesvip, int familia, String senha, Categoria categoria)
    {
        super(0, nome, cpf, telefone, email, endereco, img, sexo, situacao, datacadastro, datanascimento, credito, mesvip, familia);
        this.senha = senha;
        this.categoria = categoria;
    }

    public String getSenha()
    {
        return senha;
    }

    public void setSenha(String senha)
    {
        this.senha = senha;
    }

    public Categoria getCategoria()
    {
        return categoria;
    }

    public void setCategoria(Categoria categoria)
    {
        this.categoria = categoria;
    }

}
