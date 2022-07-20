/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mlanches.db.controladoras;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import mlanches.db.entidades.Caixa;
import mlanches.db.entidades.Recebimento;
import mlanches.db.util.Conexao;

/**
 *
 * @author thale
 */
public class CtrRecebimento
{

    public boolean salvar(Recebimento r)
    {
        try
        {
            Conexao.get().getConnect().setAutoCommit(false);
        } catch (SQLException ex)
        {
            Logger.getLogger(CtrRecebimento.class.getName()).log(Level.SEVERE, null, ex);
        }

        String sql;
        boolean status;

        sql = "insert into recebimento(datapagamento,val_pago,troco,numero,pagou,marcou,dinheiro,cartao) values('$1',$2,$3,$4,$5,$6,$7,$8)";
        sql = sql.replace("$1", r.getDatapagamento().toString());
        sql = sql.replace("$2", "" + r.getValor_pago());
        sql = sql.replace("$3", "" + r.getTroco());
        sql = sql.replace("$4", "" + r.getNumero());
        sql = sql.replace("$5", "" + r.isPagou());
        sql = sql.replace("$6", "" + r.isMarcou());
        sql = sql.replace("$7", "" + r.isDinheiro());
        sql = sql.replace("$8", "" + r.isCartao());

        if (status = Conexao.get().manipular(sql))
        {
            Caixa c = new CtrCaixa().getUltimo();
            new CtrCaixa().salvar(new Caixa(0, "aberto", LocalDateTime.now(), c.getSaldoInicial(),
                    c.getSaldoFinal() + r.getValor_pago() - r.getTroco(), mlanches.Mlanches.getInstance().Sessao));
        } else if (!status)
        {
            System.out.println(Conexao.get().getMensagemErro());
            try
            {
                Conexao.get().getConnect().rollback();
            } catch (SQLException ex)
            {
                Logger.getLogger(CtrRecebimento.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        try
        {
            Conexao.get().getConnect().setAutoCommit(true);
        } catch (SQLException ex)
        {
            Logger.getLogger(CtrRecebimento.class.getName()).log(Level.SEVERE, null, ex);
        }
        return status;
    }

    public ArrayList<Recebimento> get(Recebimento r, String condicao, String filtro)
    {
        ResultSet rs;
        ArrayList<Recebimento> alp = null;
        String sql;

        sql = "select * from recebimento";
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
                    alp.add(r = new Recebimento(rs.getTimestamp("datapagamento").toLocalDateTime(), rs.getFloat("val_pago"), rs.getFloat("troco"), rs.getInt("numero"),
                            rs.getBoolean("pagou"), rs.getBoolean("marcou"), rs.getBoolean("dinheiro"), rs.getBoolean("cartao")));
            }
        } catch (SQLException ex)
        {
            Logger.getLogger(CtrRecebimento.class.getName()).log(Level.SEVERE, null, ex);
        }

        return alp;
    }

    public ArrayList<Recebimento> get(Recebimento r, String sql)
    {
        ResultSet rs;
        ArrayList<Recebimento> alp = null;

        rs = Conexao.get().consultar(sql);
        try
        {
            if (rs != null)
            {
                alp = new ArrayList();
                while (rs.next())
                    alp.add(r = new Recebimento(rs.getTimestamp("datapagamento").toLocalDateTime(), rs.getFloat("val_pago"), rs.getFloat("troco"), rs.getInt("numero"),
                            rs.getBoolean("pagou"), rs.getBoolean("marcou"), rs.getBoolean("dinheiro"), rs.getBoolean("cartao")));
            }
        } catch (SQLException ex)
        {
            Logger.getLogger(CtrRecebimento.class.getName()).log(Level.SEVERE, null, ex);
        }
        return alp;
    }
}
