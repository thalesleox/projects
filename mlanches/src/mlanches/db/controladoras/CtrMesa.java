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
import mlanches.db.entidades.Mesa;
import mlanches.db.entidades.Pessoa;
import mlanches.db.util.Conexao;

/**
 *
 * @author thale
 */
public class CtrMesa
{

    String erro = "";

    public String getErro()
    {
        return erro;
    }

    public boolean abrir(int nMesa, int nCliente)
    {
        erro = "";
        if (this.get("select * from mesa where numero = " + nMesa + " and dataFechamento is null").isEmpty()) // Verifica se a mesa escolhida esta livre

            if (this.get("select * from mesa where pes_cod = " + nCliente + " and dataFechamento is null").isEmpty()) // Verifica a abertura de mais de 1 mesa por pessoa
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

                sql = "insert into mesa(numero,dataAbertura,pes_cod) values($1,'$2',$3)";
                sql = sql.replace("$1", "" + nMesa);
                sql = sql.replace("$2", "" + LocalDateTime.now());
                sql = sql.replace("$3", "" + nCliente);

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
                    erro = Conexao.get().getMensagemErro();
                try
                {
                    Conexao.get().getConnect().setAutoCommit(true);
                } catch (SQLException ex)
                {
                    Logger.getLogger(CtrCargo.class.getName()).log(Level.SEVERE, null, ex);
                }
                return status;
            } else
            {
                erro = "Cliente ja possui uma mesa aberta.";
                return false;
            }
        else
        {
            erro = "Mesa em uso.";
            return false;
        }
    }

    public ArrayList<Mesa> get(String sql)
    {
        ResultSet rs;
        ArrayList<Mesa> alm = null;

        rs = Conexao.get().consultar(sql);
        try
        {
            if (rs != null)
            {
                alm = new ArrayList();
                while (rs.next())
                    alm.add(new Mesa(rs.getInt(1), rs.getTimestamp(2).toLocalDateTime(), rs.getTimestamp(3) == null ? null : rs.getTimestamp(3).toLocalDateTime(),
                            new CtrPessoa().get(new Pessoa(), rs.getInt(4))));
            }
        } catch (SQLException ex)
        {
            Logger.getLogger(CtrPessoa.class.getName()).log(Level.SEVERE, null, ex);
        }

        return alm;
    }
}
