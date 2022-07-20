package rosolen.db.controladoras;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import rosolen.db.entidades.Categoria;
import rosolen.db.entidades.Endereco;
import rosolen.db.entidades.Pessoa;
import rosolen.db.entidades.Situacao;
import rosolen.db.entidades.Usuario;
import rosolen.db.util.Conexao;

public class CtrPessoa
{

    private Pessoa atual;

    public Pessoa novoUsuario(int cod, String nome, String cpf, String telefone, String email, Endereco endereco,
            BufferedImage img, char sexo, Situacao situacao, Timestamp datacadastro, Timestamp datanascimento, float credito, int mesvip, int familia, String senha, Categoria categoria)
    {
        return new Usuario(cod, nome, cpf, telefone, email, endereco, img, sexo, situacao, datacadastro, datanascimento, credito, mesvip, familia, senha, categoria);
    }

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
            case "Usuario":
                sql = "insert into pessoa(nome,cpf,telefone,email,sexo,sit_cod,datacadastro,datanascimento,credito,familia) "
                        + "values('$1','$2','$3','$4','$5',$6,'$7','$8',$9,#1)";
                sql = sql.replace("$1", p.getNome());
                sql = sql.replace("$2", p.getCpf());
                sql = sql.replace("$3", p.getTelefone());
                sql = sql.replace("$4", p.getEmail());
                sql = sql.replace("$5", "" + p.getSexo());
                sql = sql.replace("$6", "" + p.getSituacao().getCod());
                sql = sql.replace("$7", "" + p.getDatacadastro());
                sql = sql.replace("$8", "" + p.getDatanascimento());
                sql = sql.replace("$9", "" + p.getCredito());
                sql = sql.replace("#1", "" + p.getFamilia());

                if (status = Conexao.get().manipular(sql))
                {
                    p.setCod(Conexao.get().getMaxPK("pessoa", "pes_cod"));
                    p.setFamilia(p.getCod());
                    p.getEnd().setCod(p.getCod());

                    sql = "insert into usuario(pes_cod,senha,cat_cod) values($1,'$2',$3)";
                    sql = sql.replace("$1", "" + p.getCod());
                    sql = sql.replace("$2", ((Usuario) p).getSenha());
                    sql = sql.replace("$3", "" + ((Usuario) p).getCategoria().getCod());
                    if (status = Conexao.get().manipular(sql))
                        if (status = new CtrPessoa().alterar(p))
                            if (new CtrEndereco().salvar(p.getEnd()))
                                SalvarImagem(p);
                }

                if (!status)
                    try
                    {
                        Conexao.get().getConnect().rollback();
                    } catch (SQLException ex)
                    {
                        Logger.getLogger(CtrPessoa.class.getName()).log(Level.SEVERE, null, ex);
                    }
                else
                    atual = p;

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
        if (p.getCod() != 0)
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

            sql = "update pessoa set nome='$1', cpf='$2', telefone='$3', email='$4', sexo='$5', sit_cod=$6, datacadastro='$7', datanascimento='$8', familia=$9 where pes_cod=#1";
            sql = sql.replace("$1", p.getNome());
            sql = sql.replace("$2", p.getCpf());
            sql = sql.replace("$3", p.getTelefone());
            sql = sql.replace("$4", p.getEmail());
            sql = sql.replace("$5", "" + p.getSexo());
            sql = sql.replace("$6", "" + p.getSituacao().getCod());
            sql = sql.replace("$7", "" + p.getDatacadastro());
            sql = sql.replace("$8", "" + p.getDatanascimento());
            sql = sql.replace("$9", "" + p.getFamilia());
            sql = sql.replace("#1", "" + p.getCod());
            status = Conexao.get().manipular(sql);
            atual = p;

            if (status && new CtrEndereco().alterar(p.getEnd()))
                switch (tipo)
                {
                    case "Usuario":
                        sql = "update usuario set senha='$1', cat_cod=$2 where pes_cod=$3";
                        sql = sql.replace("$1", ((Usuario) p).getSenha());
                        sql = sql.replace("$2", "" + ((Usuario) p).getCategoria().getCod());
                        sql = sql.replace("$3", "" + p.getCod());
                        if (status = Conexao.get().manipular(sql))
                            status = SalvarImagem(p);

                        if (!status)
                            try
                            {

                                Conexao.get().getConnect().rollback();
                            } catch (SQLException ex)
                            {
                                Logger.getLogger(CtrPessoa.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        break;
                }

            try
            {
                Conexao.get().getConnect().setAutoCommit(true);
            } catch (SQLException ex)
            {
                Logger.getLogger(CtrPessoa.class.getName()).log(Level.SEVERE, null, ex);
            }
            return status;
        } else
            return false;
    }

    public boolean apagar(Pessoa p)
    {
        if (p.getCod() != 0)
        {
            try
            {
                Conexao.get().getConnect().setAutoCommit(false);
            } catch (SQLException ex)
            {
                Logger.getLogger(CtrPessoa.class.getName()).log(Level.SEVERE, null, ex);
            }

            boolean status = false;
            String sql = "delete from endereco where pes_cod=" + p.getCod();
            if (status = Conexao.get().manipular(sql))
            {
                sql = "delete from usuario where pes_cod=" + p.getCod();
                if (status = Conexao.get().manipular(sql))
                {
                    sql = "delete from lancamento where pes_cod=" + p.getCod();
                    if (status = Conexao.get().manipular(sql))
                    {
                        sql = "delete from pessoa where pes_cod=" + p.getCod();
                        status = Conexao.get().manipular(sql);
                    }
                }
            }

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
        } else
            return false;
    }

    public Pessoa get(Pessoa p)
    {
        String tipo = this.tipo(p);
        String sql;
        ResultSet rs;
        switch (tipo)
        {
            case "Pessoa":
                sql = "select * from pessoa where pes_cod=" + p.getCod();
                rs = Conexao.get().consultar(sql);

                try
                {
                    if (rs != null)
                        if (rs.next())
                            p = new Pessoa(rs.getInt("pes_cod"), rs.getString("nome"), rs.getString("cpf"), rs.getString("telefone"), rs.getString("email"),
                                    new CtrEndereco().get(rs.getInt("pes_cod")), obterImagem(rs.getBytes("foto")), rs.getString("sexo").charAt(0),
                                    new CtrSituacao().get(rs.getInt("sit_cod")), rs.getTimestamp("datacadastro"), rs.getTimestamp("datanascimento"),
                                    rs.getLong("credito"), rs.getInt("mesvip"), rs.getInt("familia"));
                } catch (SQLException ex)
                {
                    Logger.getLogger(CtrPessoa.class.getName()).log(Level.SEVERE, null, ex);
                }

                break;

            case "Usuario":
                sql = "select * from usuario u inner join pessoa p on u.pes_cod=p.pes_cod where u.pes_cod=" + p.getCod();
                rs = Conexao.get().consultar(sql);
                try
                {
                    if (rs != null)
                        if (rs.next())
                            p = new Usuario(rs.getInt("pes_cod"), rs.getString("nome"), rs.getString("cpf"), rs.getString("telefone"), rs.getString("email"),
                                    new CtrEndereco().get(rs.getInt("pes_cod")), obterImagem(rs.getBytes("foto")), rs.getString("sexo").charAt(0),
                                    new CtrSituacao().get(rs.getInt("sit_cod")), rs.getTimestamp("datacadastro"), rs.getTimestamp("datanascimento"),
                                    rs.getLong("credito"), rs.getInt("mesvip"), rs.getInt("familia"), rs.getString("senha"), new CtrCategoria().get(rs.getInt("cat_cod")));
                } catch (SQLException ex)
                {
                    Logger.getLogger(CtrPessoa.class.getName()).log(Level.SEVERE, null, ex);
                }
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
                sql = "select * from pessoa where pes_cod=" + cod;
                rs = Conexao.get().consultar(sql);

                try
                {
                    if (rs != null)
                        if (rs.next())
                            p = new Pessoa(rs.getInt("pes_cod"), rs.getString("nome"), rs.getString("cpf"), rs.getString("telefone"), rs.getString("email"),
                                    new CtrEndereco().get(rs.getInt("pes_cod")), obterImagem(rs.getBytes("foto")), rs.getString("sexo").charAt(0),
                                    new CtrSituacao().get(rs.getInt("sit_cod")), rs.getTimestamp("datacadastro"), rs.getTimestamp("datanascimento"),
                                    rs.getLong("credito"), rs.getInt("mesvip"), rs.getInt("familia"));
                } catch (SQLException ex)
                {
                    Logger.getLogger(CtrPessoa.class.getName()).log(Level.SEVERE, null, ex);
                }

                break;

            case "Usuario":
                sql = "select * from usuario u inner join pessoa p on u.pes_cod=p.pes_cod where u.pes_cod=" + cod;
                rs = Conexao.get().consultar(sql);
                try
                {
                    if (rs != null)
                        if (rs.next())
                            p = new Usuario(rs.getInt("pes_cod"), rs.getString("nome"), rs.getString("cpf"), rs.getString("telefone"), rs.getString("email"),
                                    new CtrEndereco().get(rs.getInt("pes_cod")), obterImagem(rs.getBytes("foto")), rs.getString("sexo").charAt(0),
                                    new CtrSituacao().get(rs.getInt("sit_cod")), rs.getTimestamp("datacadastro"), rs.getTimestamp("datanascimento"),
                                    rs.getLong("credito"), rs.getInt("mesvip"), rs.getInt("familia"), rs.getString("senha"), new CtrCategoria().get(rs.getInt("cat_cod")));
                } catch (SQLException ex)
                {
                    Logger.getLogger(CtrPessoa.class.getName()).log(Level.SEVERE, null, ex);
                }
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
                sql = "select * from pessoa";
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
                            alp.add(new Pessoa(rs.getInt("pes_cod"), rs.getString("nome"), rs.getString("cpf"), rs.getString("telefone"), rs.getString("email"),
                                    new CtrEndereco().get(rs.getInt("pes_cod")), obterImagem(rs.getBytes("foto")), rs.getString("sexo").charAt(0),
                                    new CtrSituacao().get(rs.getInt("sit_cod")), rs.getTimestamp("datacadastro"), rs.getTimestamp("datanascimento"),
                                    rs.getLong("credito"), rs.getInt("mesvip"), rs.getInt("familia")));
                    }
                } catch (SQLException ex)
                {
                    Logger.getLogger(CtrPessoa.class.getName()).log(Level.SEVERE, null, ex);
                }

                break;

            case "Usuario":
                sql = "select * from usuario u inner join pessoa p on u.pes_cod=p.pes_cod";
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
                            alp.add(new Usuario(rs.getInt("pes_cod"), rs.getString("nome"), rs.getString("cpf"), rs.getString("telefone"), rs.getString("email"),
                                    new CtrEndereco().get(rs.getInt("pes_cod")), obterImagem(rs.getBytes("foto")), rs.getString("sexo").charAt(0),
                                    new CtrSituacao().get(rs.getInt("sit_cod")), rs.getTimestamp("datacadastro"), rs.getTimestamp("datanascimento"),
                                    rs.getLong("credito"), rs.getInt("mesvip"), rs.getInt("familia"), rs.getString("senha"), new CtrCategoria().get(rs.getInt("cat_cod"))));
                    }
                } catch (SQLException ex)
                {
                    Logger.getLogger(CtrPessoa.class.getName()).log(Level.SEVERE, null, ex);
                }

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
                            alp.add(new Pessoa(rs.getInt("pes_cod"), rs.getString("nome"), rs.getString("cpf"), rs.getString("telefone"), rs.getString("email"),
                                    new CtrEndereco().get(rs.getInt("pes_cod")), obterImagem(rs.getBytes("foto")), rs.getString("sexo").charAt(0),
                                    new CtrSituacao().get(rs.getInt("sit_cod")), rs.getTimestamp("datacadastro"), rs.getTimestamp("datanascimento"),
                                    rs.getLong("credito"), rs.getInt("mesvip"), rs.getInt("familia")));
                    }
                } catch (SQLException ex)
                {
                    Logger.getLogger(CtrPessoa.class.getName()).log(Level.SEVERE, null, ex);
                }

                break;

            case "Usuario":
                rs = Conexao.get().consultar(sql);

                try
                {
                    if (rs != null)
                    {
                        alp = new ArrayList();
                        while (rs.next())
                            alp.add(new Usuario(rs.getInt("pes_cod"), rs.getString("nome"), rs.getString("cpf"), rs.getString("telefone"), rs.getString("email"),
                                    new CtrEndereco().get(rs.getInt("pes_cod")), obterImagem(rs.getBytes("foto")), rs.getString("sexo").charAt(0),
                                    new CtrSituacao().get(rs.getInt("sit_cod")), rs.getTimestamp("datacadastro"), rs.getTimestamp("datanascimento"),
                                    rs.getLong("credito"), rs.getInt("mesvip"), rs.getInt("familia"), rs.getString("senha"), new CtrCategoria().get(rs.getInt("cat_cod"))));
                    }
                } catch (SQLException ex)
                {
                    Logger.getLogger(CtrPessoa.class.getName()).log(Level.SEVERE, null, ex);
                }

                break;
        }
        return alp;
    }

    public boolean SalvarImagem(Pessoa p)
    {
        try
        {
            PreparedStatement st = Conexao.get().getConnect().prepareStatement(
                    "update pessoa set foto=? where pes_cod=?");
            if (p.getImg() == null)
            {
                st.setNull(1, java.sql.Types.NULL);
                st.setInt(2, p.getCod());
            } else
            {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(p.getImg(), "jpg", baos);
                InputStream is = new ByteArrayInputStream(baos.toByteArray());
                st.setBinaryStream(1, is, baos.toByteArray().length);
                st.setInt(2, p.getCod());
            }
            st.executeUpdate();
            return true;

        } catch (Exception e)
        {
            System.out.println(e);
        }
        return false;
    }

    public BufferedImage obterImagem(byte bytes[])
    {
        BufferedImage bi = null;
        try
        {
            if (bytes != null)
                bi = ImageIO.read(new ByteArrayInputStream(bytes));
        } catch (IOException ex)
        {
            System.out.println("Erro obter imagem: " + ex.getMessage());
        }
        return bi;
    }

    public Usuario Entrar(String usuario, String senha)
    {
        Usuario u = null;

        String sql = "select * from pessoa where email='$1'";
        sql = sql.replace("$1", usuario);
        ResultSet rs = Conexao.get().consultar(sql);

        try
        {
            if (rs.next())
            {
                u = new Usuario(rs.getInt("pes_cod"), rs.getString("nome"), rs.getString("cpf"), rs.getString("telefone"), rs.getString("email"),
                        new CtrEndereco().get(rs.getInt("pes_cod")), obterImagem(rs.getBytes("foto")), rs.getString("sexo").charAt(0),
                        new CtrSituacao().get(rs.getInt("sit_cod")), rs.getTimestamp("datacadastro"), rs.getTimestamp("datanascimento"),
                        rs.getLong("credito"), rs.getInt("mesvip"), rs.getInt("familia"), "", null);
                sql = "select * from usuario where pes_cod=$1";
                sql = sql.replace("$1", "" + rs.getInt("pes_cod"));
                rs = Conexao.get().consultar(sql);

                if (rs.next())
                    if (senha.equals(rs.getString("senha")))
                        u.setCategoria(new CtrCategoria().get(rs.getInt("cat_cod")));
                    else
                        u = null;
                else
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
