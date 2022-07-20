/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mlanches.db.controladoras;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import mlanches.db.entidades.Endereco;
import mlanches.db.util.AcessoCEP;
import mlanches.db.util.Conexao;
import org.json.JSONObject;

/**
 *
 * @author Aluno
 */
public class CtrEndereco
{

    /*
    "status":1,
    "code":"06233-030",
    "state":"SP",
    "city":"Osasco",
    "district":"Piratininga",
    "address":"Rua Paula Rodrigues"
     */
    public Endereco getCepWS(String cep)
    {
        String str = new AcessoCEP().consultaCep(cep, "json");
        JSONObject my_obj = new JSONObject(str);

        Endereco end = null;
        if (my_obj.getInt("status") == 1)
            try
            {
                end = new Endereco();
                end.setCep(cep);
                String endcompleto = my_obj.getString("address");

                if (!endcompleto.trim().isEmpty())
                {
                    String endcompletotemp = endcompleto;
                    end.setTipolog(endcompletotemp.substring(0, endcompletotemp.indexOf(' ')));
                    endcompletotemp = endcompletotemp.substring(endcompletotemp.indexOf(' ') + 1);

                    String logtemp = endcompletotemp.substring(0, 3);

                    if (logtemp.equals("das") || logtemp.equals("dos"))
                        endcompletotemp = endcompletotemp.substring(4);

                    end.setLogradouro(endcompletotemp);
                }

                end.setBairro(my_obj.getString("district"));
            } catch (Exception ex)
            {
                end = null;
            }
        else
            end = null;

        return end;
    }

    public boolean salvar(Endereco endereco)
    {
        String sql = "insert into endereco(pes_cod,cep,tipolog,logradouro,bairro,numero) values($1,'$2','$3','$4','$5',$6)";
        sql = sql.replace("$1", "" + endereco.getCod());
        sql = sql.replace("$2", endereco.getCep());
        sql = sql.replace("$3", endereco.getTipolog());
        sql = sql.replace("$4", endereco.getLogradouro());
        sql = sql.replace("$5", endereco.getBairro());
        sql = sql.replace("$6", "" + endereco.getNumero());

        return Conexao.get().manipular(sql);
    }

    public boolean alterar(Endereco endereco)
    {
        String sql = "update endereco set cep = '$2', tipolog = '$3', logradouro = '$4', bairro = '$5', numero = $6 where pes_cod = $1";
        sql = sql.replace("$1", "" + endereco.getCod());
        sql = sql.replace("$2", endereco.getCep());
        sql = sql.replace("$3", endereco.getTipolog());
        sql = sql.replace("$4", endereco.getLogradouro());
        sql = sql.replace("$5", endereco.getBairro());
        sql = sql.replace("$6", "" + endereco.getNumero());

        return Conexao.get().manipular(sql);
    }

    public boolean apagar(Endereco endereco)
    {
        String sql = "delete from endereco where pes_cod = " + endereco.getCod();

        return Conexao.get().manipular(sql);
    }

    public Endereco get(int cod)
    {
        Endereco end = null;
        String sql = "select * from endereco where pes_cod=$1";
        sql = sql.replace("$1", "" + cod);
        ResultSet rs = Conexao.get().consultar(sql);
        try
        {
            if (rs.next())
                end = new Endereco(rs.getInt("pes_cod"), rs.getString("cep"), rs.getString("tipolog"), rs.getString("logradouro"), rs.getString("bairro"), rs.getInt("numero"));
        } catch (SQLException ex)
        {
            Logger.getLogger(CtrEndereco.class.getName()).log(Level.SEVERE, null, ex);
            end = null;
        } finally
        {
            return end;
        }
    }

    public ArrayList get(String sql)
    {
        ArrayList<Endereco> alend = new ArrayList();
        ResultSet rs = Conexao.get().consultar(sql);
        try
        {
            while (rs.next())
                alend.add(new Endereco(rs.getInt("pes_cod"), rs.getString("cep"), rs.getString("tipolog"), rs.getString("logradouro"),
                        rs.getString("bairro"), rs.getInt("numero")));
        } catch (SQLException ex)
        {
            Logger.getLogger(CtrEndereco.class.getName()).log(Level.SEVERE, null, ex);
            alend = null;
        } finally
        {
            return alend;
        }
    }

}
