package mlanches.db.controladoras;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import mlanches.db.entidades.Cargo;
import mlanches.db.entidades.Pessoa;
import mlanches.db.entidades.Usuario;
import mlanches.db.util.Conexao;

public class CtrPessoa
{

    private Pessoa atual;

    public boolean salvar(Pessoa p)
    {
        try
        {
            Conexao.get().getConnect().setAutoCommit(false);
        } catch (SQLException ex)
        {
            Logger.getLogger(CtrPessoa.class.getName()).log(Level.SEVERE, null, ex);
        }

        String tipo = this.tipo(p);
        String sql;
        boolean status = false;

        switch (tipo)
        {
            case "Pessoa":
                sql = "insert into pessoa(nome,endereco,telefone,saldo,car_cod) values('$1','$2','$3',$4,$5)";
                sql = sql.replace("$1", p.getNome());
                sql = sql.replace("$2", p.getEndereco());
                sql = sql.replace("$3", p.getTelefone());
                sql = sql.replace("$4", "" + p.getSaldo());
                sql = sql.replace("$5", "" + p.getCargo().getCod());

                if (!Conexao.get().manipular(sql))
                    try
                    {
                        Conexao.get().getConnect().rollback();
                    } catch (SQLException ex)
                    {
                        Logger.getLogger(CtrPessoa.class.getName()).log(Level.SEVERE, null, ex);
                    }
                else
                {
                    status = true;
                    atual = p;
                }
                break;

            case "Usuario":
                break;
        }
        if (!status)
            System.out.println(Conexao.get().getMensagemErro());
        try
        {
            Conexao.get().getConnect().setAutoCommit(true);
        } catch (SQLException ex)
        {
            Logger.getLogger(CtrPessoa.class.getName()).log(Level.SEVERE, null, ex);
        }
        return status;
    }

    public Pessoa getAtual()
    {
        return atual;
    }

    public boolean alterar(Pessoa p)
    {
        try
        {
            Conexao.get().getConnect().setAutoCommit(false);
        } catch (SQLException ex)
        {
            Logger.getLogger(CtrPessoa.class.getName()).log(Level.SEVERE, null, ex);
        }

        String sql;
        boolean status = false;

        sql = "update pessoa set nome='$1', endereco='$2', telefone='$3', saldo='$4', car_cod=$5 where pes_cod=" + p.getCod();
        sql = sql.replace("$1", p.getNome());
        sql = sql.replace("$2", p.getEndereco());
        sql = sql.replace("$3", p.getTelefone());
        sql = sql.replace("$4", "" + p.getSaldo());
        sql = sql.replace("$5", "" + p.getCargo().getCod());
        status = Conexao.get().manipular(sql);
        atual = p;

        try
        {
            Conexao.get().getConnect().setAutoCommit(true);
        } catch (SQLException ex)
        {
            Logger.getLogger(CtrPessoa.class.getName()).log(Level.SEVERE, null, ex);
        }
        return status;
    }

    public boolean apagar(Pessoa p)
    {
        try
        {
            Conexao.get().getConnect().setAutoCommit(false);
        } catch (SQLException ex)
        {
            Logger.getLogger(CtrPessoa.class.getName()).log(Level.SEVERE, null, ex);
        }

        boolean status = false;
        String sql;
        sql = "delete from pessoa where pes_cod=" + p.getCod();
        status = Conexao.get().manipular(sql);

        if (!status)
            try
            {
                Conexao.get().getConnect().rollback();
            } catch (SQLException ex)
            {
                Logger.getLogger(CtrPessoa.class.getName()).log(Level.SEVERE, null, ex);
            }

        try
        {
            Conexao.get().getConnect().setAutoCommit(true);
        } catch (SQLException ex)
        {
            Logger.getLogger(CtrPessoa.class.getName()).log(Level.SEVERE, null, ex);
        }
        return status;
    }

    public Pessoa get(Pessoa p)
    {
        String tipo = this.tipo(p);
        String sql;
        ResultSet rs;
        switch (tipo)
        {
            case "Pessoa":
                sql = "select * from pessoa where pes_cod=" + p.getCod() + " and pes_cod != 0";
                rs = Conexao.get().consultar(sql);

                try
                {
                    if (rs != null)
                        if (rs.next())
                            p = new Pessoa(rs.getInt("pes_cod"), rs.getString("nome"), rs.getString("endereco"), rs.getString("telefone"), rs.getLong("saldo"),
                                    new CtrCargo().get(new Cargo(), rs.getInt("car_cod")));
                } catch (SQLException ex)
                {
                    Logger.getLogger(CtrPessoa.class.getName()).log(Level.SEVERE, null, ex);
                }

                break;

            case "Usuario":
                break;
        }

        return p;
    }

    public Pessoa get(Pessoa p, int cod)
    {
        String tipo = this.tipo(p);
        String sql;
        ResultSet rs;
        switch (tipo)
        {
            case "Pessoa":
                sql = "select * from pessoa where pes_cod=" + cod + " and pes_cod != 0";
                ;
                rs = Conexao.get().consultar(sql);

                try
                {
                    if (rs != null)
                        if (rs.next())
                            p = new Pessoa(rs.getInt("pes_cod"), rs.getString("nome"), rs.getString("endereco"), rs.getString("telefone"), rs.getLong("saldo"),
                                    new CtrCargo().get(new Cargo(), rs.getInt("car_cod")));
                } catch (SQLException ex)
                {
                    Logger.getLogger(CtrPessoa.class.getName()).log(Level.SEVERE, null, ex);
                }

                break;

            case "Usuario":
                break;
        }

        return p;
    }

    public ArrayList<Pessoa> get(Pessoa p, String condicao, String filtro)
    {
        String tipo = this.tipo(p);
        ResultSet rs;
        ArrayList<Pessoa> alp = null;
        String sql = "";

        switch (tipo)
        {
            case "Pessoa":
                sql = "select * from pessoa where pes_cod != 0";
                if (!condicao.isEmpty())
                    sql += " and " + condicao;
                if (!filtro.isEmpty())
                    sql += " " + filtro;
                rs = Conexao.get().consultar(sql);

                try
                {
                    if (rs != null)
                    {
                        alp = new ArrayList();
                        while (rs.next())
                            alp.add(p = new Pessoa(rs.getInt("pes_cod"), rs.getString("nome"), rs.getString("endereco"), rs.getString("telefone"), rs.getLong("saldo"),
                                    new CtrCargo().get(new Cargo(), rs.getInt("car_cod"))));
                    }
                } catch (SQLException ex)
                {
                    Logger.getLogger(CtrPessoa.class.getName()).log(Level.SEVERE, null, ex);
                }

                break;

            case "Usuario":
                break;
        }

        return alp;
    }

    public ArrayList<Pessoa> get(Pessoa p, String sql)
    {
        String tipo = this.tipo(p);
        ResultSet rs;
        ArrayList<Pessoa> alp = null;

        switch (tipo)
        {
            case "Pessoa":
                rs = Conexao.get().consultar(sql);
                try
                {
                    if (rs != null)
                    {
                        alp = new ArrayList();
                        while (rs.next())
                            alp.add(p = new Pessoa(rs.getInt("pes_cod"), rs.getString("nome"), rs.getString("endereco"), rs.getString("telefone"), rs.getLong("saldo"),
                                    new CtrCargo().get(new Cargo(), rs.getInt("car_cod"))));
                    }
                } catch (SQLException ex)
                {
                    Logger.getLogger(CtrPessoa.class.getName()).log(Level.SEVERE, null, ex);
                }

                break;

            case "Usuario":
                break;
        }
        return alp;
    }

    public Usuario Entrar(String usuario, String senha)
    {
        Usuario u = null;

        String sql = "select * from pessoa where nome like '$1'";
        sql = sql.replace("$1", usuario);
        ResultSet rs = Conexao.get().consultar(sql);

        try
        {
            if (rs.next())
            {
                u = new Usuario(rs.getInt("pes_cod"),
                        rs.getString("nome"),
                        rs.getString("endereco"),
                        rs.getString("telefone"),
                        rs.getFloat("saldo"),
                        new CtrCargo().get(new Cargo(), rs.getInt("car_cod")), "");

                sql = "select * from usuario where usu_cod=$1";
                sql = sql.replace("$1", "" + rs.getInt("pes_cod"));
                rs = Conexao.get().consultar(sql);

                if (rs.next())
                {
                    if (!senha.equals(rs.getString("senha")))
                        u = null;
                } else
                    u = null;
            }
        } catch (SQLException ex)
        {
            Logger.getLogger(CtrPessoa.class.getName()).log(Level.SEVERE, null, ex);
            u = null;
        }

        return u;
    }

    public String tipo(Pessoa p)
    {
        return p.getClass().getSimpleName();
    }

}
