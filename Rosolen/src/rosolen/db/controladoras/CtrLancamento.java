/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rosolen.db.controladoras;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import rosolen.db.entidades.Lancamento;
import rosolen.db.entidades.Pessoa;
import rosolen.db.util.Conexao;

/**
 *
 * @author thale
 */
public class CtrLancamento
{

    public boolean salvar(Lancamento lancamento)
    {
        String sql = "insert into lancamento(pes_cod,aca_cod,datamens,datapagamento,avaliacao,mensalidade,descricao,total) "
                + "values($1,$2,'$3','$4',$5,$6,'$7',$8)";
        sql = sql.replace("$1", "" + lancamento.getCliente().getCod());
        sql = sql.replace("$2", "" + lancamento.getAcademia().getCod());
        sql = sql.replace("$3", lancamento.getDataMens().toString());
        sql = sql.replace("$4", lancamento.getDataPagamento().toString());
        sql = sql.replace("$5", "" + lancamento.isAvaliacao());
        sql = sql.replace("$6", "" + lancamento.isMensalidade());
        sql = sql.replace("$7", "" + lancamento.getDescricao());
        sql = sql.replace("$8", "" + lancamento.getTotal());
        return Conexao.get().manipular(sql);
    }

    public ArrayList<Lancamento> getAll(String filtro, String filtro2)
    {
        String sql = "select * from lancamento";
        if (!filtro.trim().isEmpty())
            sql += " where " + filtro;
        if (!filtro2.trim().isEmpty())
            sql += " " + filtro2;

        ResultSet rs = Conexao.get().consultar(sql);
        ArrayList<Lancamento> al = new ArrayList();
        try
        {
            while (rs.next())
            {
                Pessoa cliente = new Pessoa();
                cliente.setCod(rs.getInt("pes_cod"));
                Pessoa academia = new Pessoa();
                academia.setCod(rs.getInt("aca_cod"));

                al.add(new Lancamento(rs.getInt("lan_cod"), new CtrPessoa().get(cliente), new CtrPessoa().get(academia), rs.getTimestamp("datamens"), rs.getTimestamp("datapagamento"), rs.getBoolean("avaliacao"),
                        rs.getBoolean("mensalidade"), rs.getString("descricao"), rs.getFloat("total")));

            }
        } catch (SQLException ex)
        {
            Logger.getLogger(CtrLancamento.class.getName()).log(Level.SEVERE, null, ex);
        }
        return al;
    }

    public ArrayList<Lancamento> getAll(String sql)
    {
        ArrayList<Lancamento> al = new ArrayList();
        if (!sql.trim().isEmpty())
        {
            ResultSet rs = Conexao.get().consultar(sql);
            try
            {
                while (rs.next())
                {
                    Pessoa cliente = new Pessoa();
                    cliente.setCod(rs.getInt("pes_cod"));
                    Pessoa academia = new Pessoa();
                    academia.setCod(rs.getInt("aca_cod"));

                    al.add(new Lancamento(rs.getInt("lan_cod"), new CtrPessoa().get(cliente), new CtrPessoa().get(academia), rs.getTimestamp("datamens"), rs.getTimestamp("datapagamento"), rs.getBoolean("avaliacao"),
                            rs.getBoolean("mensalidade"), rs.getString("descricao"), rs.getFloat("total")));

                }
            } catch (SQLException ex)
            {
                Logger.getLogger(CtrLancamento.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return al;
    }

    public LocalDate ultimaMensalidade(int cod)
    {
        LocalDate ld = LocalDate.of(1900, 1, 1);
        String sql = "select max(datamens) from lancamento where pes_cod = $1 and mensalidade = true";
        sql = sql.replace("$1", "" + cod);
        ResultSet rs = Conexao.get().consultar(sql);
        try
        {
            if (rs.next())
                if (rs.getTimestamp("max") != null)
                    ld = rs.getTimestamp("max").toLocalDateTime().toLocalDate();
        } catch (SQLException ex)
        {
            Logger.getLogger(CtrLancamento.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ld;
    }

    public LocalDate ultimaAvaliacao(int cod)
    {
        LocalDate ld = LocalDate.of(1900, 1, 1);
        String sql = "select max(datamens) from lancamento where pes_cod = $1 and avaliacao = true";
        sql = sql.replace("$1", "" + cod);
        ResultSet rs = Conexao.get().consultar(sql);
        try
        {
            if (rs.next())
                if (rs.getTimestamp("max") != null)
                    ld = rs.getTimestamp("max").toLocalDateTime().toLocalDate();
        } catch (SQLException ex)
        {
            Logger.getLogger(CtrLancamento.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ld;
    }
}
