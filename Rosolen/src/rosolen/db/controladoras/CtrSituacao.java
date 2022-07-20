/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rosolen.db.controladoras;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import rosolen.db.entidades.Situacao;
import rosolen.db.util.Conexao;

/**
 *
 * @author thale
 */
public class CtrSituacao
{

    public boolean salvar(Situacao s)
    {
        String sql;
        boolean status = false;
        sql = "insert into situacao(sit_cod,descricao) "
                + "values($1,'$2')";
        sql = sql.replace("$1", "" + s.getCod());
        sql = sql.replace("$2", s.getDescricao());

        return Conexao.get().manipular(sql);
    }

    public boolean alterar(Situacao s)
    {
        String sql = "update situacao set";
        sql += " descricao='$1' where sit_cod=$2";
        sql = sql.replace("$1", s.getDescricao());
        sql = sql.replace("$2", "" + s.getCod());
        return Conexao.get().manipular(sql);
    }

    public boolean apagar(Situacao s)
    {
        return Conexao.get().manipular("delete from situacao where sit_cod=" + s.getCod());
    }

    public Situacao get(int cod)
    {
        ResultSet rs;
        String sql = "select * from situacao where sit_cod=" + cod;
        rs = Conexao.get().consultar(sql);
        Situacao s = null;
        try
        {
            if (rs.next())
                s = new Situacao(rs.getInt("sit_cod"), rs.getString("descricao"));
        } catch (SQLException ex)
        {
            Logger.getLogger(CtrPessoa.class.getName()).log(Level.SEVERE, null, ex);
        }
        return s;
    }

    public ArrayList<Situacao> getAll()
    {
        String sql = "select * from situacao";
        ResultSet rs = Conexao.get().consultar(sql);
        ArrayList<Situacao> al = new ArrayList();
        try
        {
            while (rs.next())
                al.add(new Situacao(rs.getInt("sit_cod"), rs.getString("descricao")));
        } catch (SQLException ex)
        {
            Logger.getLogger(CtrSituacao.class.getName()).log(Level.SEVERE, null, ex);
            al = null;
        }
        return al;
    }

    public String tipo(Situacao s)
    {
        return s.getClass().getSimpleName();
    }
}
