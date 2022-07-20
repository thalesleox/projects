package mlanches.db.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Conexao
{

    private static Conexao con = null;
    private Connection connect;
    private String erro;

    private Conexao(String local, String banco, String usuario, String senha)
    {
        erro = "";
        try
        {
            //Class.forName(driver); "org.postgresql.Driver");
            String url = local + banco; //"jdbc:postgresql://localhost/"+banco;
            connect = DriverManager.getConnection(url, usuario, senha);
        } catch (SQLException sqlex)
        {
            erro = "Impossivel conectar com a base de dados: " + sqlex.toString();
        } catch (Exception ex)
        {
            erro = "Outro erro: " + ex.toString();
        }
    }

    public static Conexao get()
    {
        if (con == null)
            con = new Conexao("jdbc:postgresql://localhost:5432/", "mineirinho", "postgres", "postgres123");
        return con;
    }

    public boolean conectar(String local, String banco, String usuario, String senha)
    {
        boolean conectado = false;
        try
        {
            //Class.forName(driver); "org.postgresql.Driver");
            String url = local + banco; //"jdbc:postgresql://localhost/"+banco;
            connect = DriverManager.getConnection(url, usuario, senha);
            conectado = true;
        } catch (SQLException sqlex)
        {
            erro = "Impossivel conectar com a base de dados: " + sqlex.toString();
        } catch (Exception ex)
        {
            erro = "Outro erro: " + ex.toString();
        }
        return conectado;
    }

    public String getMensagemErro()
    {
        return erro;
    }

    public boolean getEstadoConexao()
    {
        return (connect != null);
    }

    public boolean manipular(String sql) // inserir, alterar,excluir
    {
        boolean executou = false;
        try
        {
            Statement statement = connect.createStatement();
            int result = statement.executeUpdate(sql);
            statement.close();
            if (result >= 1)
                executou = true;
        } catch (SQLException sqlex)
        {
            erro = "Erro: " + sqlex.toString();
        }
        return executou;
    }

    public ResultSet consultar(String sql)
    {
        ResultSet rs = null;
        try
        {
            Statement statement = connect.createStatement();
            //ResultSet.TYPE_SCROLL_INSENSITIVE,
            //ResultSet.CONCUR_READ_ONLY);
            rs = statement.executeQuery(sql);
            //statement.close();
        } catch (SQLException sqlex)
        {
            erro = "Erro: " + sqlex.toString();
            rs = null;
        }
        return rs;
    }

    public int getMaxPK(String tabela, String chave)
    {
        String sql = "select max(" + chave + ") from " + tabela;
        int max = 0;
        ResultSet rs = consultar(sql);
        try
        {
            if (rs.next())
                max = rs.getInt(1);
        } catch (SQLException sqlex)
        {
            erro = "Erro: " + sqlex.toString();
            max = -1;
        }
        return max;
    }

    public Connection getConnect()
    {
        return connect;
    }

    static public boolean BackupRestore(String arq)
    {
        Runtime r = Runtime.getRuntime();
        try
        {
            Process p = r.exec("bkp\\" + arq);
            if (p != null)
            {
                InputStreamReader str = new InputStreamReader(p.getErrorStream());
                BufferedReader reader = new BufferedReader(str);
                String linha;
                while ((linha = reader.readLine()) != null)
                    System.out.println(linha);
            }
            return true;
        } catch (IOException ex)
        {
            return false;
        }
    }
}
