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
import mlanches.db.entidades.Acrescimo;
import mlanches.db.entidades.UnidadeMedida;
import mlanches.db.util.Conexao;

/**
 *
 * @author thale
 */
public class CtrAcrescimo
{

    public boolean salvar(Acrescimo ac)
    {
        try
        {
            Conexao.get().getConnect().setAutoCommit(false);
        } catch (SQLException ex)
        {
            Logger.getLogger(CtrAcrescimo.class.getName()).log(Level.SEVERE, null, ex);
        }

        String sql;
        boolean status = false;

        sql = "insert into acrescimo(mp_cod,valor) values($1,$2)";
        sql = sql.replace("$1", "" + ac.getCod());
        sql = sql.replace("$2", "" + ac.getValor());
        status = Conexao.get().manipular(sql);

        if (!status)
            System.out.println(Conexao.get().getMensagemErro());
        try
        {
            Conexao.get().getConnect().setAutoCommit(true);
        } catch (SQLException ex)
        {
            Logger.getLogger(CtrAcrescimo.class.getName()).log(Level.SEVERE, null, ex);
        }
        return status;
    }

    public boolean alterar(Acrescimo ac)
    {
        try
        {
            Conexao.get().getConnect().setAutoCommit(false);
        } catch (SQLException ex)
        {
            Logger.getLogger(CtrAcrescimo.class.getName()).log(Level.SEVERE, null, ex);
        }

        String sql;
        boolean status = false;

        sql = "update acrescimo set valor=$ where mp_cod=" + ac.getCod();
        sql = sql.replace("$1", "" + ac.getValor());
        status = Conexao.get().manipular(sql);

        try
        {
            Conexao.get().getConnect().setAutoCommit(true);
        } catch (SQLException ex)
        {
            Logger.getLogger(CtrAcrescimo.class.getName()).log(Level.SEVERE, null, ex);
        }
        return status;
    }

    public boolean apagar(Acrescimo ac)
    {
        try
        {
            Conexao.get().getConnect().setAutoCommit(false);
        } catch (SQLException ex)
        {
            Logger.getLogger(CtrAcrescimo.class.getName()).log(Level.SEVERE, null, ex);
        }

        boolean status = false;
        String sql;
        sql = "delete from acrescimo where mp_cod=" + ac.getCod();
        status = Conexao.get().manipular(sql);

        if (!status)
            try
            {
                Conexao.get().getConnect().rollback();
            } catch (SQLException ex)
            {
                Logger.getLogger(CtrAcrescimo.class.getName()).log(Level.SEVERE, null, ex);
            }

        try
        {
            Conexao.get().getConnect().setAutoCommit(true);
        } catch (SQLException ex)
        {
            Logger.getLogger(CtrAcrescimo.class.getName()).log(Level.SEVERE, null, ex);
        }
        return status;
    }

    public Acrescimo get(Acrescimo ac)
    {
        String sql;
        ResultSet rs;
        sql = "select * from acrescimo where mp_cod=" + ac.getCod();
        rs = Conexao.get().consultar(sql);

        try
        {
            if (rs != null)
                if (rs.next())
                    ac = new Acrescimo(rs.getInt("mp_cod"), rs.getString("descricao"), new CtrUnidadeMedida().get(new UnidadeMedida(), rs.getInt("um_cod")), rs.getFloat("valor"));
        } catch (SQLException ex)
        {
            Logger.getLogger(CtrAcrescimo.class.getName()).log(Level.SEVERE, null, ex);
        }

        return ac;
    }

    public Acrescimo get(Acrescimo ac, int cod)
    {
        String sql;
        ResultSet rs;
        sql = "select * from acrescimo where mp_cod=" + cod;
        rs = Conexao.get().consultar(sql);

        try
        {
            if (rs != null)
                if (rs.next())
                    ac = new Acrescimo(rs.getInt("mp_cod"), rs.getString("descricao"), new CtrUnidadeMedida().get(new UnidadeMedida(), rs.getInt("um_cod")), rs.getFloat("valor"));
        } catch (SQLException ex)
        {
            Logger.getLogger(CtrAcrescimo.class.getName()).log(Level.SEVERE, null, ex);
        }

        return ac;
    }

    public ArrayList<Acrescimo> get(Acrescimo ac, String condicao, String filtro)
    {
        ResultSet rs;
        ArrayList<Acrescimo> alp = null;
        String sql;

        sql = "select * from acrescimo";
        if (!condicao.isEmpty())
            sql += " where " + condicao;
        if (!filtro.isEmpty())
            sql += " " + filtro;
        rs = Conexao.get().consultar(sql);

        try
        {
            if (rs != null)
            {
                alp = new ArrayList();
                while (rs.next())
                    alp.add(ac = new Acrescimo(rs.getInt("mp_cod"), rs.getString("descricao"), new CtrUnidadeMedida().get(new UnidadeMedida(), rs.getInt("um_cod")), rs.getFloat("valor")));
            }
        } catch (SQLException ex)
        {
            Logger.getLogger(CtrAcrescimo.class.getName()).log(Level.SEVERE, null, ex);
        }

        return alp;
    }

    public ArrayList<Acrescimo> get(Acrescimo ac, String sql)
    {
        ResultSet rs;
        ArrayList<Acrescimo> alp = null;

        rs = Conexao.get().consultar(sql);
        try
        {
            if (rs != null)
            {
                alp = new ArrayList();
                while (rs.next())
                    alp.add(ac = new Acrescimo(rs.getInt("mp_cod"), rs.getString("descricao"), new CtrUnidadeMedida().get(new UnidadeMedida(), rs.getInt("um_cod")), rs.getFloat("valor")));
            }
        } catch (SQLException ex)
        {
            Logger.getLogger(CtrAcrescimo.class.getName()).log(Level.SEVERE, null, ex);
        }
        return alp;
    }
}
