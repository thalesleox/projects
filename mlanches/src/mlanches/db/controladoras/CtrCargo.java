package mlanches.db.controladoras;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import mlanches.db.entidades.Cargo;
import mlanches.db.entidades.Cargo;
import mlanches.db.util.Conexao;

public class CtrCargo
{

    public boolean salvar(Cargo c)
    {
        try
        {
            Conexao.get().getConnect().setAutoCommit(false);
        } catch (SQLException ex)
        {
            Logger.getLogger(CtrCargo.class.getName()).log(Level.SEVERE, null, ex);
        }

        String sql;
        boolean status = false;

        sql = "insert into cargo(descricao) values('$1')";
        sql = sql.replace("$1", c.getDescricao());

        if (!Conexao.get().manipular(sql))
            try
            {
                Conexao.get().getConnect().rollback();
            } catch (SQLException ex)
            {
                Logger.getLogger(CtrCargo.class.getName()).log(Level.SEVERE, null, ex);
            }

        if (!status)
            System.out.println(Conexao.get().getMensagemErro());
        try
        {
            Conexao.get().getConnect().setAutoCommit(true);
        } catch (SQLException ex)
        {
            Logger.getLogger(CtrCargo.class.getName()).log(Level.SEVERE, null, ex);
        }
        return status;
    }

    public boolean alterar(Cargo c)
    {
        try
        {
            Conexao.get().getConnect().setAutoCommit(false);
        } catch (SQLException ex)
        {
            Logger.getLogger(CtrCargo.class.getName()).log(Level.SEVERE, null, ex);
        }

        String sql;
        boolean status = false;

        sql = "update cargo set descricao='$1' where car_cod=" + c.getCod();
        sql = sql.replace("$1", c.getDescricao());
        status = Conexao.get().manipular(sql);

        try
        {
            Conexao.get().getConnect().setAutoCommit(true);
        } catch (SQLException ex)
        {
            Logger.getLogger(CtrCargo.class.getName()).log(Level.SEVERE, null, ex);
        }
        return status;
    }

    public boolean apagar(Cargo c)
    {
        try
        {
            Conexao.get().getConnect().setAutoCommit(false);
        } catch (SQLException ex)
        {
            Logger.getLogger(CtrCargo.class.getName()).log(Level.SEVERE, null, ex);
        }

        boolean status = false;
        String sql;
        sql = "delete from cargo where car_cod=" + c.getCod();
        status = Conexao.get().manipular(sql);

        if (!status)
            try
            {
                Conexao.get().getConnect().rollback();
            } catch (SQLException ex)
            {
                Logger.getLogger(CtrCargo.class.getName()).log(Level.SEVERE, null, ex);
            }

        try
        {
            Conexao.get().getConnect().setAutoCommit(true);
        } catch (SQLException ex)
        {
            Logger.getLogger(CtrCargo.class.getName()).log(Level.SEVERE, null, ex);
        }
        return status;
    }

    public Cargo get(Cargo c)
    {
        String sql;
        ResultSet rs;
        sql = "select * from cargo where car_cod=" + c.getCod();
        rs = Conexao.get().consultar(sql);

        try
        {
            if (rs != null)
                if (rs.next())
                    c = new Cargo(rs.getInt("car_cod"), rs.getString("descricao"));
        } catch (SQLException ex)
        {
            Logger.getLogger(CtrCargo.class.getName()).log(Level.SEVERE, null, ex);
        }

        return c;
    }

    public Cargo get(Cargo c, int cod)
    {
        String sql;
        ResultSet rs;
        sql = "select * from cargo where car_cod=" + cod;
        rs = Conexao.get().consultar(sql);

        try
        {
            if (rs != null)
                if (rs.next())
                    c = new Cargo(rs.getInt("car_cod"), rs.getString("descricao"));
        } catch (SQLException ex)
        {
            Logger.getLogger(CtrCargo.class.getName()).log(Level.SEVERE, null, ex);
        }

        return c;
    }

    public ArrayList<Cargo> get(Cargo c, String condicao, String filtro)
    {
        ResultSet rs;
        ArrayList<Cargo> alp = null;
        String sql;

        sql = "select * from cargo";
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
                    alp.add(c = new Cargo(rs.getInt("car_cod"), rs.getString("descricao")));
            }
        } catch (SQLException ex)
        {
            Logger.getLogger(CtrCargo.class.getName()).log(Level.SEVERE, null, ex);
        }

        return alp;
    }

    public ArrayList<Cargo> get(Cargo c, String sql)
    {
        ResultSet rs;
        ArrayList<Cargo> alp = null;

        rs = Conexao.get().consultar(sql);
        try
        {
            if (rs != null)
            {
                alp = new ArrayList();
                while (rs.next())
                    alp.add(c = new Cargo(rs.getInt("car_cod"), rs.getString("descricao")));
            }
        } catch (SQLException ex)
        {
            Logger.getLogger(CtrCargo.class.getName()).log(Level.SEVERE, null, ex);
        }
        return alp;
    }

}
