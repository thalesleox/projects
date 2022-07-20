/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rosolen.db.entidades;

import java.awt.image.BufferedImage;
import java.sql.Timestamp;

/**
 *
 * @author Aluno
 */
public class Pessoa
{

    private int cod;
    private String nome;
    private String cpf;
    private String telefone;
    private String email;
    private BufferedImage img;
    private Endereco end;
    private char sexo;
    private Situacao situacao;
    private Timestamp datacadastro;
    private Timestamp datanascimento;
    private float credito;
    private int mesvip;
    private int familia;

    public Pessoa(int cod, String nome, String cpf, String telefone, String email, Endereco end,
            BufferedImage img, char sexo, Situacao situacao, Timestamp datacadastro, Timestamp datanascimento, float credito, int mesvip, int familia)
    {
        this.cod = cod;
        this.nome = nome;
        this.cpf = cpf;
        this.telefone = telefone;
        this.email = email;
        this.end = end;
        this.img = img;
        this.sexo = sexo;
        this.situacao = situacao;
        this.datacadastro = datacadastro;
        this.datanascimento = datanascimento;
        this.credito = credito;
        this.mesvip = mesvip;
        this.familia = familia;
    }

    public Pessoa()
    {
        this(0, "", "", "", "", null, null, 'i', null, null, null, 0, 0, 0);
    }

    public Pessoa(String nome, String cpf, String telefone, String email, Endereco end,
            BufferedImage img, char sexo, Situacao situacao, Timestamp datacadastro, Timestamp datanascimento, float credito, int mesvip, int familia)
    {
        this(0, nome, cpf, telefone, email, end, img, sexo, situacao, datacadastro, datanascimento, credito, mesvip, familia);
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

    public String getCpf()
    {
        return cpf;
    }

    public void setCpf(String cpf)
    {
        this.cpf = cpf;
    }

    public String getTelefone()
    {
        return telefone;
    }

    public void setTelefone(String telefone)
    {
        this.telefone = telefone;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public BufferedImage getImg()
    {
        return img;
    }

    public void setImg(BufferedImage img)
    {
        this.img = img;
    }

    public char getSexo()
    {
        return sexo;
    }

    public void setSexo(char sexo)
    {
        this.sexo = sexo;
    }

    public Situacao getSituacao()
    {
        return situacao;
    }

    public void setSituacao(Situacao situacao)
    {
        this.situacao = situacao;
    }

    public Timestamp getDatacadastro()
    {
        return datacadastro;
    }

    public void setDatacadastro(Timestamp datacadastro)
    {
        this.datacadastro = datacadastro;
    }

    public Timestamp getDatanascimento()
    {
        return datanascimento;
    }

    public void setDatanascimento(Timestamp datanascimento)
    {
        this.datanascimento = datanascimento;
    }

    public float getCredito()
    {
        return credito;
    }

    public void setCredito(float credito)
    {
        this.credito = credito;
    }

    public int getMesvip()
    {
        return mesvip;
    }

    public void setMesvip(int mesvip)
    {
        this.mesvip = mesvip;
    }

    public Endereco getEnd()
    {
        return end;
    }

    public void setEnd(Endereco end)
    {
        this.end = end;
    }

    public int getFamilia()
    {
        return familia;
    }

    public void setFamilia(int familia)
    {
        this.familia = familia;
    }

}
