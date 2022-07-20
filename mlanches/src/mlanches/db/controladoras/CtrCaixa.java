/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mlanches.db.controladoras;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import mlanches.db.entidades.Caixa;
import mlanches.db.entidades.Pessoa;
import mlanches.db.util.Conexao;

/**
 *
 * @author thale
 */
public class CtrCaixa
{

    public boolean salvar(Caixa c)
    {
        try
        {
            Conexao.get().getConnect().setAutoCommit(false);
        } catch (SQLException ex)
        {
            Logger.getLogger(CtrCargo.class.getName()).log(Level.SEVERE, null, ex);
        }

        String sql;
        boolean status;

        sql = "insert into caixa(status,data,saldoinicial,saldofinal,pes_cod) values('$1','$2',$3,$4,$5)";
        sql = sql.replace("$1", c.getStatus());
        sql = sql.replace("$2", "" + c.getData());
        sql = sql.replace("$3", "" + c.getSaldoInicial());
        sql = sql.replace("$4", "" + c.getSaldoFinal());
        sql = sql.replace("$5", "" + mlanches.Mlanches.getInstance().getSessao().getCod());

        status = Conexao.get().manipular(sql);
        if (!status)
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

    public Caixa get(String sql)
    {
        ResultSet rs = Conexao.get().consultar(sql);
        Caixa c = null;
        try
        {
            if (rs != null)
                if (rs.next())
                    c = new Caixa(rs.getInt(1), rs.getString(2), rs.getTimestamp(3).toLocalDateTime(), rs.getFloat(4),
                            rs.getFloat(5), new CtrPessoa().get(new Pessoa(), rs.getInt(6)));
        } catch (SQLException ex)
        {
            Logger.getLogger(CtrCargo.class.getName()).log(Level.SEVERE, null, ex);
        }

        return c;
    }

    public Caixa getUltimo()
    {
        return this.get("select * from caixa order by cx_cod desc limit 1");
    }
}
