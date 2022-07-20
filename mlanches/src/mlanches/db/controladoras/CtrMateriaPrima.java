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
import mlanches.db.entidades.MateriaPrima;
import mlanches.db.entidades.UnidadeMedida;
import mlanches.db.util.Conexao;

/**
 *
 * @author thale
 */
public class CtrMateriaPrima
{

    public boolean salvar(MateriaPrima mp)
    {
        try
        {
            Conexao.get().getConnect().setAutoCommit(false);
        } catch (SQLException ex)
        {
            Logger.getLogger(CtrMateriaPrima.class.getName()).log(Level.SEVERE, null, ex);
        }

        String sql;
        boolean status = false;

        sql = "insert into materiaprima(descricao,um_cod) values('$1',$2)";
        sql = sql.replace("$1", mp.getDescricao());
        sql = sql.replace("$2", "" + mp.getUnidadeMedida().getCod());
        status = Conexao.get().manipular(sql);

        if (!status)
            System.out.println(Conexao.get().getMensagemErro());
        try
        {
            Conexao.get().getConnect().setAutoCommit(true);
        } catch (SQLException ex)
        {
            Logger.getLogger(CtrMateriaPrima.class.getName()).log(Level.SEVERE, null, ex);
        }
        return status;
    }

    public boolean alterar(MateriaPrima mp)
    {
        try
        {
            Conexao.get().getConnect().setAutoCommit(false);
        } catch (SQLException ex)
        {
            Logger.getLogger(CtrMateriaPrima.class.getName()).log(Level.SEVERE, null, ex);
        }

        String sql;
        boolean status = false;

        sql = "update materiaprima set descricao='$1', um_cod=$2 where mp_cod=" + mp.getCod();
        sql = sql.replace("$1", mp.getDescricao());
        sql = sql.replace("$2", "" + mp.getUnidadeMedida().getCod());
        status = Conexao.get().manipular(sql);

        try
        {
            Conexao.get().getConnect().setAutoCommit(true);
        } catch (SQLException ex)
        {
            Logger.getLogger(CtrMateriaPrima.class.getName()).log(Level.SEVERE, null, ex);
        }
        return status;
    }

    public boolean apagar(MateriaPrima mp)
    {
        try
        {
            Conexao.get().getConnect().setAutoCommit(false);
        } catch (SQLException ex)
        {
            Logger.getLogger(CtrMateriaPrima.class.getName()).log(Level.SEVERE, null, ex);
        }

        boolean status = false;
        String sql;
        sql = "delete from materiaprima where mp_cod=" + mp.getCod();
        status = Conexao.get().manipular(sql);

        if (!status)
            try
            {
                Conexao.get().getConnect().rollback();
            } catch (SQLException ex)
            {
                Logger.getLogger(CtrMateriaPrima.class.getName()).log(Level.SEVERE, null, ex);
            }

        try
        {
            Conexao.get().getConnect().setAutoCommit(true);
        } catch (SQLException ex)
        {
            Logger.getLogger(CtrMateriaPrima.class.getName()).log(Level.SEVERE, null, ex);
        }
        return status;
    }

    public MateriaPrima get(MateriaPrima mp)
    {
        String sql;
        ResultSet rs;
        sql = "select * from materiaprima where mp_cod=" + mp.getCod();
        rs = Conexao.get().consultar(sql);

        try
        {
            if (rs != null)
                if (rs.next())
                    mp = new MateriaPrima(rs.getInt("mp_cod"), rs.getString("descricao"), new CtrUnidadeMedida().get(new UnidadeMedida(), rs.getInt("um_cod")));
        } catch (SQLException ex)
        {
            Logger.getLogger(CtrMateriaPrima.class.getName()).log(Level.SEVERE, null, ex);
        }

        return mp;
    }

    public MateriaPrima get(MateriaPrima mp, int cod)
    {
        String sql;
        ResultSet rs;
        sql = "select * from materiaprima where mp_cod=" + cod;
        rs = Conexao.get().consultar(sql);

        try
        {
            if (rs != null)
                if (rs.next())
                    mp = new MateriaPrima(rs.getInt("mp_cod"), rs.getString("descricao"), new CtrUnidadeMedida().get(new UnidadeMedida(), rs.getInt("um_cod")));
        } catch (SQLException ex)
        {
            Logger.getLogger(CtrMateriaPrima.class.getName()).log(Level.SEVERE, null, ex);
        }

        return mp;
    }

    public ArrayList<MateriaPrima> get(MateriaPrima mp, String condicao, String filtro)
    {
        ResultSet rs;
        ArrayList<MateriaPrima> alp = null;
        String sql;

        sql = "select * from materiaprima";
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
                    alp.add(mp = new MateriaPrima(rs.getInt("mp_cod"), rs.getString("descricao"), new CtrUnidadeMedida().get(new UnidadeMedida(), rs.getInt("um_cod"))));
            }
        } catch (SQLException ex)
        {
            Logger.getLogger(CtrMateriaPrima.class.getName()).log(Level.SEVERE, null, ex);
        }

        return alp;
    }

    public ArrayList<MateriaPrima> get(MateriaPrima mp, String sql)
    {
        ResultSet rs;
        ArrayList<MateriaPrima> alp = null;

        rs = Conexao.get().consultar(sql);
        try
        {
            if (rs != null)
            {
                alp = new ArrayList();
                while (rs.next())
                    alp.add(mp = new MateriaPrima(rs.getInt("mp_cod"), rs.getString("descricao"), new CtrUnidadeMedida().get(new UnidadeMedida(), rs.getInt("um_cod"))));
            }
        } catch (SQLException ex)
        {
            Logger.getLogger(CtrMateriaPrima.class.getName()).log(Level.SEVERE, null, ex);
        }
        return alp;
    }
}
