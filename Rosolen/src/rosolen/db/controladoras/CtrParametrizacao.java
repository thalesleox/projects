/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rosolen.db.controladoras;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.imageio.ImageIO;
import rosolen.db.entidades.Parametrizacao;
import rosolen.db.util.Conexao;

/**
 *
 * @author Aluno
 */
public class CtrParametrizacao
{

    private Parametrizacao par = null;

    public boolean salvar(Parametrizacao par)
    {
        if (par != null)
        {
            apagar();
            String str = "insert into parametrizacao(par_cod,razaosocial,telefone) "
                    + "values(1,'$1','$2')";
            str = str.replace("$1", par.getRazaosoc());
            str = str.replace("$2", par.getTelefone());

            return Conexao.get().manipular(str) && new CtrEndereco().salvar(par.getEndereco()) && SalvarImagem(par);
        } else
            return false;
    }

    public boolean apagar()
    {
        return Conexao.get().manipular("delete from parametrizacao where par_cod=1");
    }

    public Parametrizacao get()
    {
        if (par == null)
        {
            String sql = "select * from parametrizacao where par_cod=1";
            ResultSet rs = Conexao.get().consultar(sql);

            try
            {
                if (rs.next())
                    par = new Parametrizacao(rs.getString("razaosocial"), new CtrEndereco().get(0), rs.getString("telefone"), obterImagem(rs.getBytes("logo")));
            } catch (SQLException e)
            {
                System.out.println("ERRO: " + e.getMessage());
            } finally
            {
                return par;
            }
        } else
            return par;
    }

    public Parametrizacao getPar()
    {
        return par;
    }

    public BufferedImage obterImagem(byte bytes[])
    {
        try
        {
            return ImageIO.read(new ByteArrayInputStream(bytes));
        } catch (IOException ex)
        {
            System.out.println("Erro obter imagem: " + ex.getMessage());
            return null;
        }
    }

    private boolean SalvarImagem(Parametrizacao p)
    {
        boolean estado = false;
        try
        {
            PreparedStatement st = Conexao.get().getConnect().prepareStatement(
                    "update parametrizacao set logo=? where par_cod=?");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(p.getBufferedImageLogo(), "jpg", baos);
            InputStream is = new ByteArrayInputStream(baos.toByteArray());
            st.setBinaryStream(1, is, baos.toByteArray().length);
            st.setInt(2, 1);
            st.executeUpdate();
            estado = true;

        } catch (IOException | SQLException e)
        {
            System.out.println(e);
        }
        return estado;
    }
}
