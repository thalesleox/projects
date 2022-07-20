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
import mlanches.db.entidades.Produto;
import mlanches.db.entidades.UnidadeMedida;
import mlanches.db.util.Conexao;

/**
 *
 * @author thale
 */
public class CtrProduto
{

    public boolean salvar(Produto p)
    {
        try
        {
            Conexao.get().getConnect().setAutoCommit(false);
        } catch (SQLException ex)
        {
            Logger.getLogger(CtrProduto.class.getName()).log(Level.SEVERE, null, ex);
        }

        String sql;
        boolean status = false;

        sql = "insert into produto(descricao,um_cod,valor) values('$1',$2,'$3')";
        sql = sql.replace("$1", p.getDescricao());
        sql = sql.replace("$2", "" + p.getUnidadeMedida().getCod());
        sql = sql.replace("$3", "" + p.getValor());
        status = Conexao.get().manipular(sql);

        if (status)
        {
            p.setCod(Conexao.get().getMaxPK("produto", "pro_cod"));
            if (p.getIngredientes() != null && p.getIngredientes().size() > 0)
                for (MateriaPrima mp : p.getIngredientes())
                {
                    sql = "insert into ingrediente(pro_cod,mp_cod) values($1,$2)";
                    sql = sql.replace("$1", "" + p.getCod());
                    sql = sql.replace("$2", "" + mp.getCod());
                    Conexao.get().manipular(sql);
                }
        }

        if (!status)
        {
            System.out.println(Conexao.get().getMensagemErro());
            try
            {
                Conexao.get().getConnect().rollback();
            } catch (SQLException ex)
            {
                Logger.getLogger(CtrProduto.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        try
        {
            Conexao.get().getConnect().setAutoCommit(true);
        } catch (SQLException ex)
        {
            Logger.getLogger(CtrProduto.class.getName()).log(Level.SEVERE, null, ex);
        }
        return status;
    }

    public boolean alterar(Produto p)
    {
        try
        {
            Conexao.get().getConnect().setAutoCommit(false);
        } catch (SQLException ex)
        {
            Logger.getLogger(CtrProduto.class.getName()).log(Level.SEVERE, null, ex);
        }

        String sql;
        boolean status = false;

        sql = "update produto set descricao='$1', um_cod=$2, valor='$3' where pro_cod=" + p.getCod();
        sql = sql.replace("$1", p.getDescricao());
        sql = sql.replace("$2", "" + p.getUnidadeMedida().getCod());
        sql = sql.replace("$3", "" + p.getValor());
        if (status = Conexao.get().manipular(sql))
            sql = "delete from ingrediente where pro_cod=" + p.getCod();
        if (Conexao.get().manipular(sql))
            if (p.getIngredientes() != null && p.getIngredientes().size() > 0)
                for (MateriaPrima mp : p.getIngredientes())
                {
                    sql = "insert into ingrediente(pro_cod,mp_cod) values($1,$2)";
                    sql = sql.replace("$1", "" + p.getCod());
                    sql = sql.replace("$2", "" + mp.getCod());
                    Conexao.get().manipular(sql);
                }

        if (!status)
        {
            System.out.println(Conexao.get().getMensagemErro());
            try
            {
                Conexao.get().getConnect().rollback();
            } catch (SQLException ex)
            {
                Logger.getLogger(CtrProduto.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        try
        {
            Conexao.get().getConnect().setAutoCommit(true);
        } catch (SQLException ex)
        {
            Logger.getLogger(CtrProduto.class.getName()).log(Level.SEVERE, null, ex);
        }
        return status;
    }

    public boolean apagar(Produto p)
    {
        try
        {
            Conexao.get().getConnect().setAutoCommit(false);
        } catch (SQLException ex)
        {
            Logger.getLogger(CtrProduto.class.getName()).log(Level.SEVERE, null, ex);
        }

        boolean status = false;
        String sql;
        sql = "delete from produto where pro_cod=$1";
        sql = sql.replace("$1", "" + p.getCod());
        status = Conexao.get().manipular(sql);

        if (!status)
            try
            {
                Conexao.get().getConnect().rollback();
            } catch (SQLException ex)
            {
                Logger.getLogger(CtrProduto.class.getName()).log(Level.SEVERE, null, ex);
            }

        try
        {
            Conexao.get().getConnect().setAutoCommit(true);
        } catch (SQLException ex)
        {
            Logger.getLogger(CtrProduto.class.getName()).log(Level.SEVERE, null, ex);
        }
        return status;
    }

    private ArrayList<MateriaPrima> getIngredientes(int cod)
    {
        String sql;
        ResultSet rs;
        sql = "select * from ingrediente where pro_cod=" + cod;
        rs = Conexao.get().consultar(sql);
        ArrayList<MateriaPrima> almp = null;

        try
        {
            if (rs != null)
            {
                almp = new ArrayList<>();
                while (rs.next())
                    almp.add(new CtrMateriaPrima().get(new MateriaPrima(), rs.getInt("mp_cod")));
            }
        } catch (SQLException ex)
        {
            Logger.getLogger(CtrProduto.class.getName()).log(Level.SEVERE, null, ex);
        }

        return almp;
    }

    public Produto get(Produto p)
    {
        String sql;
        ResultSet rs;
        sql = "select * from produto where um_cod=" + p.getCod();
        rs = Conexao.get().consultar(sql);

        try
        {
            if (rs != null)
                if (rs.next())
                    p = new Produto(rs.getInt("um_cod"), rs.getString("descricao"), new CtrUnidadeMedida().get(new UnidadeMedida(), rs.getInt("um_cod")),
                            rs.getFloat("valor"), getIngredientes(p.getCod()), rs.getString("tag"));
        } catch (SQLException ex)
        {
            Logger.getLogger(CtrProduto.class.getName()).log(Level.SEVERE, null, ex);
        }

        return p;
    }

    public Produto get(Produto p, int cod)
    {
        String sql;
        ResultSet rs;
        sql = "select * from produto where um_cod=" + cod;
        rs = Conexao.get().consultar(sql);

        try
        {
            if (rs != null)
                if (rs.next())
                    p = new Produto(rs.getInt("um_cod"), rs.getString("descricao"), new CtrUnidadeMedida().get(new UnidadeMedida(), rs.getInt("um_cod")),
                            rs.getFloat("valor"), getIngredientes(p.getCod()), rs.getString("tag"));
        } catch (SQLException ex)
        {
            Logger.getLogger(CtrProduto.class.getName()).log(Level.SEVERE, null, ex);
        }

        return p;
    }

    public ArrayList<Produto> get(Produto p, String condicao, String filtro)
    {
        ResultSet rs;
        ArrayList<Produto> alp = null;
        String sql;

        sql = "select * from produto";
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
                    alp.add(p = new Produto(rs.getInt("pro_cod"), rs.getString("descricao"), new CtrUnidadeMedida().get(new UnidadeMedida(), rs.getInt("um_cod")),
                            rs.getFloat("valor"), getIngredientes(p.getCod()), rs.getString("tag")));
            }
        } catch (SQLException ex)
        {
            Logger.getLogger(CtrProduto.class.getName()).log(Level.SEVERE, null, ex);
        }

        return alp;
    }

    public ArrayList<Produto> get(Produto p, String sql)
    {
        ResultSet rs;
        ArrayList<Produto> alp = null;

        rs = Conexao.get().consultar(sql);
        try
        {
            if (rs != null)
            {
                alp = new ArrayList();
                while (rs.next())
                    alp.add(p = new Produto(rs.getInt("um_cod"), rs.getString("descricao"), new CtrUnidadeMedida().get(new UnidadeMedida(), rs.getInt("um_cod")),
                            rs.getFloat("valor"), getIngredientes(p.getCod()), rs.getString("tag")));
            }
        } catch (SQLException ex)
        {
            Logger.getLogger(CtrProduto.class.getName()).log(Level.SEVERE, null, ex);
        }
        return alp;
    }
}
