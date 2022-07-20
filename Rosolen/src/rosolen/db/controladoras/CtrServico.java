/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rosolen.db.controladoras;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import rosolen.db.entidades.Servico;
import rosolen.db.util.Conexao;

/**
 *
 * @author thale
 */
public class CtrServico
{

    public boolean salvar(Servico servico)
    {
        String sql = "insert into servico(descricao,valor) values('$1',$2)";
        sql = sql.replace("$1", servico.getDescricao());
        sql = sql.replace("$2", "" + servico.getValor());
        return Conexao.get().manipular(sql);
    }

    public Servico get(int cod)
    {
        String sql = "select * from servico where ser_cod=" + cod;
        ResultSet rs = Conexao.get().consultar(sql);
        Servico ser = null;
        try
        {
            if (rs.next())
                ser = new Servico(rs.getInt("ser_cod"), rs.getString("descricao"), rs.getFloat("valor"));
        } catch (SQLException ex)
        {
            Logger.getLogger(CtrServico.class.getName()).log(Level.SEVERE, null, ex);
            ser = null;
        }
        return ser;
    }

    public Servico get(String descricao)
    {
        String sql = "select * from servico where descricao ilike '%$1%'";
        sql = sql.replace("$1", descricao);
        ResultSet rs = Conexao.get().consultar(sql);
        Servico cid = null;
        try
        {
            if (rs.next())
                cid = new Servico(rs.getInt("ser_cod"), rs.getString("descricao"), rs.getFloat("valor"));
        } catch (SQLException ex)
        {
            Logger.getLogger(CtrServico.class.getName()).log(Level.SEVERE, null, ex);
        } finally
        {
            return cid;
        }
    }

    public ArrayList<Servico> getAll(String filtro, String filtro2)
    {
        String sql = "select * from servico";
        if (!filtro.isEmpty())
            sql += " where " + filtro;
        if (!filtro2.isEmpty())
            sql += " " + filtro2;

        ResultSet rs = Conexao.get().consultar(sql);
        ArrayList<Servico> al = new ArrayList();
        try
        {
            while (rs.next())
                al.add(new Servico(rs.getInt("ser_cod"), rs.getString("descricao"), rs.getFloat("valor")));
        } catch (SQLException ex)
        {
            Logger.getLogger(CtrServico.class.getName()).log(Level.SEVERE, null, ex);
        }
        return al;
    }
}
