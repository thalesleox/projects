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
import rosolen.db.entidades.Categoria;
import rosolen.db.util.Conexao;

/**
 *
 * @author thale
 */
public class CtrCategoria
{

    public boolean salvar(Categoria c)
    {
        String sql;
        boolean status = false;
        sql = "insert into categoria(cat_cod,descricao) "
                + "values($1,'$2')";
        sql = sql.replace("$1", "" + c.getCod());
        sql = sql.replace("$2", c.getDescricao());

        return Conexao.get().manipular(sql);
    }

    public boolean alterar(Categoria c)
    {
        String sql = "update categoria set";
        sql += " descricao='$1' where cat_cod=$2";
        sql = sql.replace("$1", c.getDescricao());
        sql = sql.replace("$2", "" + c.getCod());
        return Conexao.get().manipular(sql);
    }

    public boolean apagar(Categoria c)
    {
        return Conexao.get().manipular("delete from categoria where cat_cod=" + c.getCod());
    }

    public Categoria get(int cod)
    {
        ResultSet rs;
        String sql = "select * from categoria where cat_cod=" + cod;
        rs = Conexao.get().consultar(sql);
        Categoria c = null;
        try
        {
            if (rs.next())
                c = new Categoria(rs.getInt("cat_cod"), rs.getString("descricao"));
        } catch (SQLException ex)
        {
            Logger.getLogger(CtrPessoa.class.getName()).log(Level.SEVERE, null, ex);
        }
        return c;
    }

    public ArrayList<Categoria> getAll()
    {
        String sql = "select * from categoria";
        ResultSet rs = Conexao.get().consultar(sql);
        ArrayList<Categoria> al = new ArrayList();
        try
        {
            while (rs.next())
                al.add(new Categoria(rs.getInt("cat_cod"), rs.getString("descricao")));
        } catch (SQLException ex)
        {
            Logger.getLogger(CtrCategoria.class.getName()).log(Level.SEVERE, null, ex);
            al = null;
        }
        return al;
    }

    public String tipo(Categoria c)
    {
        return c.getClass().getSimpleName();
    }
}
