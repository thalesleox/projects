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
import mlanches.db.entidades.UnidadeMedida;
import mlanches.db.util.Conexao;

/**
 *
 * @author thale
 */
public class CtrUnidadeMedida
{

    public boolean salvar(UnidadeMedida um)
    {
        try
        {
            Conexao.get().getConnect().setAutoCommit(false);
        } catch (SQLException ex)
        {
            Logger.getLogger(CtrUnidadeMedida.class.getName()).log(Level.SEVERE, null, ex);
        }

        String sql;
        boolean status = false;

        sql = "insert into unidademedida(descricao,sigla) values('$1','$2')";
        sql = sql.replace("$1", um.getDescricao());
        sql = sql.replace("$2", um.getSigla());

        if (!Conexao.get().manipular(sql))
            try
            {
                Conexao.get().getConnect().rollback();
            } catch (SQLException ex)
            {
                Logger.getLogger(CtrUnidadeMedida.class.getName()).log(Level.SEVERE, null, ex);
            }

        if (!status)
            System.out.println(Conexao.get().getMensagemErro());
        try
        {
            Conexao.get().getConnect().setAutoCommit(true);
        } catch (SQLException ex)
        {
            Logger.getLogger(CtrUnidadeMedida.class.getName()).log(Level.SEVERE, null, ex);
        }
        return status;
    }

    public boolean alterar(UnidadeMedida um)
    {
        try
        {
            Conexao.get().getConnect().setAutoCommit(false);
        } catch (SQLException ex)
        {
            Logger.getLogger(CtrUnidadeMedida.class.getName()).log(Level.SEVERE, null, ex);
        }

        String sql;
        boolean status = false;

        sql = "update unidademedida set descricao='$1', sigla='$2' where um_cod=" + um.getCod();
        sql = sql.replace("$1", um.getDescricao());
        sql = sql.replace("$2", um.getSigla());
        status = Conexao.get().manipular(sql);

        try
        {
            Conexao.get().getConnect().setAutoCommit(true);
        } catch (SQLException ex)
        {
            Logger.getLogger(CtrUnidadeMedida.class.getName()).log(Level.SEVERE, null, ex);
        }
        return status;
    }

    public boolean apagar(UnidadeMedida um)
    {
        try
        {
            Conexao.get().getConnect().setAutoCommit(false);
        } catch (SQLException ex)
        {
            Logger.getLogger(CtrUnidadeMedida.class.getName()).log(Level.SEVERE, null, ex);
        }

        boolean status = false;
        String sql;
        sql = "delete from unidademedida where um_cod=" + um.getCod();
        status = Conexao.get().manipular(sql);

        if (!status)
            try
            {
                Conexao.get().getConnect().rollback();
            } catch (SQLException ex)
            {
                Logger.getLogger(CtrUnidadeMedida.class.getName()).log(Level.SEVERE, null, ex);
            }

        try
        {
            Conexao.get().getConnect().setAutoCommit(true);
        } catch (SQLException ex)
        {
            Logger.getLogger(CtrUnidadeMedida.class.getName()).log(Level.SEVERE, null, ex);
        }
        return status;
    }

    public UnidadeMedida get(UnidadeMedida um)
    {
        String sql;
        ResultSet rs;
        sql = "select * from unidademedida where um_cod=" + um.getCod();
        rs = Conexao.get().consultar(sql);

        try
        {
            if (rs != null)
                if (rs.next())
                    um = new UnidadeMedida(rs.getInt("um_cod"), rs.getString("descricao"), rs.getString("sigla"));
        } catch (SQLException ex)
        {
            Logger.getLogger(CtrUnidadeMedida.class.getName()).log(Level.SEVERE, null, ex);
        }

        return um;
    }

    public UnidadeMedida get(UnidadeMedida um, int cod)
    {
        String sql;
        ResultSet rs;
        sql = "select * from unidademedida where um_cod=" + cod;
        rs = Conexao.get().consultar(sql);

        try
        {
            if (rs != null)
                if (rs.next())
                    um = new UnidadeMedida(rs.getInt("um_cod"), rs.getString("descricao"), rs.getString("sigla"));
        } catch (SQLException ex)
        {
            Logger.getLogger(CtrUnidadeMedida.class.getName()).log(Level.SEVERE, null, ex);
        }

        return um;
    }

    public ArrayList<UnidadeMedida> get(UnidadeMedida um, String condicao, String filtro)
    {
        ResultSet rs;
        ArrayList<UnidadeMedida> alp = null;
        String sql;

        sql = "select * from unidademedida";
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
                    alp.add(um = new UnidadeMedida(rs.getInt("um_cod"), rs.getString("descricao"), rs.getString("sigla")));
            }
        } catch (SQLException ex)
        {
            Logger.getLogger(CtrUnidadeMedida.class.getName()).log(Level.SEVERE, null, ex);
        }

        return alp;
    }

    public ArrayList<UnidadeMedida> get(UnidadeMedida um, String sql)
    {
        ResultSet rs;
        ArrayList<UnidadeMedida> alp = null;

        rs = Conexao.get().consultar(sql);
        try
        {
            if (rs != null)
            {
                alp = new ArrayList();
                while (rs.next())
                    alp.add(um = new UnidadeMedida(rs.getInt("um_cod"), rs.getString("descricao"), rs.getString("sigla")));
            }
        } catch (SQLException ex)
        {
            Logger.getLogger(CtrUnidadeMedida.class.getName()).log(Level.SEVERE, null, ex);
        }
        return alp;
    }

}
