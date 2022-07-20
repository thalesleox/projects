package mlanches.db.controladoras;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import mlanches.db.entidades.Pedido;
import mlanches.db.entidades.Pessoa;
import mlanches.db.util.Conexao;

public class CtrPedido
{

    private Pedido atual;

    public boolean salvar(Pedido p)
    {
        try
        {
            Conexao.get().getConnect().setAutoCommit(false);
        } catch (SQLException ex)
        {
            Logger.getLogger(CtrPedido.class.getName()).log(Level.SEVERE, null, ex);
        }

        String tipo = this.tipo(p);
        String sql;
        boolean status = false;

        switch (tipo)
        {
            case "Pedido":
                sql = "insert into pedido(pes_cod,fun_cod,horasRealizado,horasSaida,horasChegada,descricao,obs,total,troco,"
                        + "tipoPedido,cancelado,mesa,pago) values($1,$2,'$3',$4,$5,'$6','$7',$8,$9,'#10',#11,#12,#13)";
                sql = sql.replace("$1", "" + p.getPessoa().getCod());
                sql = sql.replace("$2", "" + mlanches.Mlanches.getInstance().Sessao.getCod());
                sql = sql.replace("$3", "" + Timestamp.valueOf(LocalDateTime.now()));
                sql = sql.replace("$4", "NULL");
                sql = sql.replace("$5", "NULL");
                sql = sql.replace("$6", p.getDescricao());
                sql = sql.replace("$7", p.getObservacao());
                sql = sql.replace("$8", "" + p.getTotal());
                sql = sql.replace("$9", "" + p.getTroco());
                sql = sql.replace("#10", "" + p.getTipoPedido());
                sql = sql.replace("#11", "" + p.isCancelado());
                sql = sql.replace("#12", "" + p.getMesa());
                sql = sql.replace("#13", "" + p.isPago());

                if (!Conexao.get().manipular(sql))
                    try
                    {
                        Conexao.get().getConnect().rollback();
                    } catch (SQLException ex)
                    {
                        Logger.getLogger(CtrPedido.class.getName()).log(Level.SEVERE, null, ex);
                    }
                else
                {
                    status = true;
                    atual = p;
                }
                break;
        }
        if (!status)
            System.out.println(Conexao.get().getMensagemErro());
        try
        {
            Conexao.get().getConnect().setAutoCommit(true);
        } catch (SQLException ex)
        {
            Logger.getLogger(CtrPedido.class.getName()).log(Level.SEVERE, null, ex);
        }
        return status;
    }

    public Pedido getAtual()
    {
        return atual;
    }

    public boolean alterar(Pedido p)
    {
        try
        {
            Conexao.get().getConnect().setAutoCommit(false);
        } catch (SQLException ex)
        {
            Logger.getLogger(CtrPedido.class.getName()).log(Level.SEVERE, null, ex);
        }

        String sql;
        boolean status = false;

        sql = "update pedido set horasSaida=$1, horasChegada=$2 where ped_cod=" + p.getCod();
        sql = sql.replace("$1", p.getHorarioSaida() == null ? "null" : "'" + p.getHorarioSaida().toString()) + "'";
        sql = sql.replace("$2", p.getHorariochegada() == null ? "null" : "'" + p.getHorariochegada().toString()) + "'";

        status = Conexao.get().manipular(sql);
        atual = p;

        try
        {
            Conexao.get().getConnect().setAutoCommit(true);
        } catch (SQLException ex)
        {
            Logger.getLogger(CtrPedido.class.getName()).log(Level.SEVERE, null, ex);
        }
        return status;
    }

    public boolean apagar(Pedido p)
    {
        try
        {
            Conexao.get().getConnect().setAutoCommit(false);
        } catch (SQLException ex)
        {
            Logger.getLogger(CtrPedido.class.getName()).log(Level.SEVERE, null, ex);
        }

        boolean status = false;
        String sql;
        sql = "delete from pedido where ped_cod=" + p.getCod();
        status = Conexao.get().manipular(sql);

        if (!status)
            try
            {
                Conexao.get().getConnect().rollback();
            } catch (SQLException ex)
            {
                Logger.getLogger(CtrPedido.class.getName()).log(Level.SEVERE, null, ex);
            }

        try
        {
            Conexao.get().getConnect().setAutoCommit(true);
        } catch (SQLException ex)
        {
            Logger.getLogger(CtrPedido.class.getName()).log(Level.SEVERE, null, ex);
        }
        return status;
    }

    public Pedido get(Pedido p)
    {
        String tipo = this.tipo(p);
        String sql;
        ResultSet rs;
        switch (tipo)
        {
            case "Pedido":
                sql = "select * from pedido where ped_cod=" + p.getCod();
                rs = Conexao.get().consultar(sql);

                try
                {
                    if (rs != null)
                        if (rs.next())
                            p = new Pedido(rs.getInt("pes_cod"), new CtrPessoa().get(new Pessoa(), rs.getInt("pes_cod")), new CtrPessoa().get(new Pessoa(), rs.getInt("fun_cod")),
                                    rs.getTimestamp("horasRealizado").toLocalDateTime(), rs.getTimestamp("horasSaida").toLocalDateTime(), rs.getTimestamp("horasChegada").toLocalDateTime(),
                                    rs.getString("descricao"), rs.getString("obs"), rs.getLong("total"), rs.getLong("troco"), rs.getString("tipoPedido"), rs.getBoolean("cancelado"),
                                    rs.getInt("mesa"), rs.getBoolean("pago"));
                } catch (SQLException ex)
                {
                    Logger.getLogger(CtrPedido.class.getName()).log(Level.SEVERE, null, ex);
                }

                break;

            case "Usuario":
                break;
        }

        return p;
    }

    public Pedido get(Pedido p, int cod)
    {
        String tipo = this.tipo(p);
        String sql;
        ResultSet rs;
        switch (tipo)
        {
            case "Pedido":
                sql = "select * from pedido where ped_cod=" + cod;
                rs = Conexao.get().consultar(sql);

                try
                {
                    if (rs != null)
                        if (rs.next())
                            p = new Pedido(rs.getInt("pes_cod"), new CtrPessoa().get(new Pessoa(), rs.getInt("pes_cod")), new CtrPessoa().get(new Pessoa(), rs.getInt("fun_cod")),
                                    rs.getTimestamp("horasRealizado").toLocalDateTime(), rs.getTimestamp("horasSaida").toLocalDateTime(), rs.getTimestamp("horasChegada").toLocalDateTime(),
                                    rs.getString("descricao"), rs.getString("obs"), rs.getLong("total"), rs.getLong("troco"), rs.getString("tipoPedido"), rs.getBoolean("cancelado"),
                                    rs.getInt("mesa"), rs.getBoolean("pago"));
                } catch (SQLException ex)
                {
                    Logger.getLogger(CtrPedido.class.getName()).log(Level.SEVERE, null, ex);
                }

                break;

            case "Usuario":
                break;
        }

        return p;
    }

    public ArrayList<Pedido> get(Pedido p, String condicao, String filtro)
    {
        String tipo = this.tipo(p);
        ResultSet rs;
        ArrayList<Pedido> alp = null;
        String sql = "";

        switch (tipo)
        {
            case "Pedido":
                sql = "select * from pedido";
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
                            alp.add(p = new Pedido(rs.getInt("pes_cod"), new CtrPessoa().get(new Pessoa(), rs.getInt("pes_cod")), new CtrPessoa().get(new Pessoa(), rs.getInt("fun_cod")),
                                    rs.getTimestamp("horasRealizado").toLocalDateTime(), rs.getTimestamp("horasSaida").toLocalDateTime(), rs.getTimestamp("horasChegada").toLocalDateTime(),
                                    rs.getString("descricao"), rs.getString("obs"), rs.getLong("total"), rs.getLong("troco"), rs.getString("tipoPedido"), rs.getBoolean("cancelado"),
                                    rs.getInt("mesa"), rs.getBoolean("pago")));
                    }
                } catch (SQLException ex)
                {
                    Logger.getLogger(CtrPedido.class.getName()).log(Level.SEVERE, null, ex);
                }

                break;

            case "Usuario":
                break;
        }

        return alp;
    }

    public boolean pagar(int ped_cod, int mesa)
    {
        try
        {
            Conexao.get().getConnect().setAutoCommit(false);
        } catch (SQLException ex)
        {
            Logger.getLogger(CtrPedido.class.getName()).log(Level.SEVERE, null, ex);
        }

        String sql;
        boolean status = false;

        sql = "update pedido set pago=true where ped_cod=" + ped_cod;

        if (status = Conexao.get().manipular(sql))
        {
            if (mesa == 0)
            {
                sql = "update pedido set horaschegada='$1' where ped_cod=" + ped_cod;
                sql = sql.replace("$1", "" + Timestamp.valueOf(LocalDateTime.now()));
            } else
            {
                sql = "update mesa set datafechamento='$1' where numero=" + mesa;
                sql = sql.replace("$1", "" + Timestamp.valueOf(LocalDateTime.now()));
            }
            status = Conexao.get().manipular(sql);
        }

        if (!status)
            try
            {
                Conexao.get().getConnect().rollback();
            } catch (SQLException ex)
            {
                Logger.getLogger(CtrPedido.class.getName()).log(Level.SEVERE, null, ex);
            }

        try
        {
            Conexao.get().getConnect().setAutoCommit(true);
        } catch (SQLException ex)
        {
            Logger.getLogger(CtrPedido.class.getName()).log(Level.SEVERE, null, ex);
        }
        return status;
    }

    public ArrayList<Pedido> get(Pedido p, String sql)
    {
        String tipo = this.tipo(p);
        ResultSet rs;
        ArrayList<Pedido> alp = null;

        switch (tipo)
        {
            case "Pedido":
                rs = Conexao.get().consultar(sql);
                try
                {
                    if (rs != null)
                    {
                        alp = new ArrayList();
                        while (rs.next())
                            alp.add(p = new Pedido(rs.getInt("pes_cod"), new CtrPessoa().get(new Pessoa(), rs.getInt("pes_cod")), new CtrPessoa().get(new Pessoa(), rs.getInt("fun_cod")),
                                    rs.getTimestamp("horasRealizado").toLocalDateTime(), rs.getTimestamp("horasSaida").toLocalDateTime(), rs.getTimestamp("horasChegada").toLocalDateTime(),
                                    rs.getString("descricao"), rs.getString("obs"), rs.getLong("total"), rs.getLong("troco"), rs.getString("tipoPedido"), rs.getBoolean("cancelado"),
                                    rs.getInt("mesa"), rs.getBoolean("pago")));
                    }
                } catch (SQLException ex)
                {
                    Logger.getLogger(CtrPedido.class.getName()).log(Level.SEVERE, null, ex);
                }

                break;

            case "Usuario":
                break;
        }
        return alp;
    }

    public String tipo(Pedido p)
    {
        return p.getClass().getSimpleName();
    }

}
