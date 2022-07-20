/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rosolen;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import rosolen.UI.LoginController;
import rosolen.UI.ParametrizacaoController;
import rosolen.UI.TelaPrincipalController;
import rosolen.db.controladoras.CtrLancamento;
import rosolen.db.controladoras.CtrParametrizacao;
import rosolen.db.controladoras.CtrPessoa;
import rosolen.db.entidades.Lancamento;
import rosolen.db.entidades.Pessoa;
import rosolen.db.entidades.Usuario;
import rosolen.db.util.Conexao;

/**
 *
 * @author thale
 */
public class Rosolen extends Application
{

    private Stage Todo;
    public static Rosolen instance;

    public Rosolen()
    {
        instance = this;
    }

    public static Rosolen getInstance()
    {
        return instance;
    }

    public Usuario Sessao;

    @Override
    public void start(Stage stage) throws Exception
    {
        if (Conexao.get().getEstadoConexao())
        {
            atualizarSituacao();
            stage.getIcons().add(new Image(getClass().getResourceAsStream("imagens/logo1.png")));
            stage.setTitle("Academia Rosolen");
            Todo = stage;
            if (new CtrParametrizacao().get() == null)
                setAreaTrabalho(ParametrizacaoController.FXML);
            else
                carregarLogin();
            stage.show();
        } else
        {
            Alert al = new Alert(Alert.AlertType.ERROR, Conexao.get().getMensagemErro(), ButtonType.OK);
            al.showAndWait();
        }

    }

    public Image getImagem(String path)
    {
        return new Image(getClass().getResourceAsStream(path));
    }

    public Usuario getSessao()
    {
        return this.Sessao;
    }

    public boolean Logar(boolean validacao, Usuario user)
    {
        if (validacao)
        {
            this.Sessao = user;
            carregarPrograma();
            return true;
        } else
            return false;
    }

    public void Deslogar()
    {
        this.Sessao = null;
        carregarLogin();
    }

    private void carregarPrograma()
    {
        try
        {
            setAreaTrabalho(TelaPrincipalController.FXML);
        } catch (Exception ex)
        {
            Logger.getLogger(Rosolen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void carregarLogin()
    {
        try
        {
            setAreaTrabalho(LoginController.FXML);
        } catch (Exception ex)
        {
            Logger.getLogger(Rosolen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private Parent setAreaTrabalho(String fxml) throws Exception
    {

        Parent page = carregarFXML(fxml);
        Scene scene = Todo.getScene();
        if (scene == null)
            Todo.setScene(new Scene(page));
        else
            Todo.getScene().setRoot(page);
        Todo.setResizable(false);
        Todo.sizeToScene();
        Todo.centerOnScreen();
        return page;
    }

    public void addCSS(Scene s)
    {
        s.getStylesheets().addAll(getClass().getResource("app.css").toExternalForm());
    }

    public void addCSS(Parent p)
    {
        p.getScene().getStylesheets().addAll(getClass().getResource("app.css").toExternalForm());
    }

    public Parent carregarFXML(String fxml) throws Exception
    {
        Parent page = FXMLLoader.load(getClass().getResource("UI/" + fxml));
        page.getStylesheets().addAll(getClass().getResource("app.css").toExternalForm());
        return page;
    }

    public void ajustarTela()
    {
        if (Todo != null)
        {
            this.Todo.sizeToScene();
            this.Todo.centerOnScreen();
        }
    }

    public void atualizarSituacao()
    {
        new Thread(()
                ->
        {
            ArrayList<Pessoa> alp = new CtrPessoa().get(new Pessoa(), "sit_cod = 2 and pes_cod <> 0", "");
            if (alp != null && alp.size() > 0)
                for (Pessoa p : alp)
                {
                    LocalDate ld = new CtrLancamento().ultimaMensalidade(p.getCod());
                    if (ld != null && LocalDate.now().compareTo(ld.plusMonths(3)) > 0)
                    {
                        String sql = "update pessoa set sit_cod=3 where pes_cod=" + p.getCod();
                        Conexao.get().manipular(sql);
                    }
                }

            alp = new CtrPessoa().get(new Pessoa(), "sit_cod = 1 and pes_cod <> 0", "");
            if (alp != null && alp.size() > 0)
                for (Pessoa p : alp)
                {
                    LocalDate ld = new CtrLancamento().ultimaMensalidade(p.getCod());
                    if (ld != null && LocalDate.now().compareTo(ld.plusMonths(3)) > 0)
                    {
                        String sql = "update pessoa set sit_cod=2 where pes_cod=" + p.getCod();
                        Conexao.get().manipular(sql);
                    }
                }

            alp = new CtrPessoa().get(new Pessoa(), "sit_cod = 1 and pes_cod <> 0", "");
            if (alp != null && alp.size() > 0)
                for (Pessoa p : alp)
                    if (p.getSituacao().getCod() == 1)
                    {
                        ArrayList<Lancamento> allan = new CtrLancamento().getAll("pes_cod = " + p.getCod() + " and mensalidade = true", "order by datamens desc");
                        if (allan != null && allan.size() > 11)
                        {
                            boolean valido = true;
                            int i;
                            for (i = 0; i < allan.size() - 1 && valido; i++)
                            {
                                int aux = allan.get(i).getDataMens().toLocalDateTime().getMonthValue();
                                if (aux == 1)
                                {
                                    if (allan.get(i + 1).getDataMens().toLocalDateTime().getMonthValue() != 12)
                                        valido = false;
                                } else if (aux - 1 != allan.get(i + 1).getDataMens().toLocalDateTime().getMonthValue())
                                    valido = false;
                            }

                            if (valido || i >= 11)
                            {
                                String sql = "update pessoa set sit_cod=4, mesvip = " + (i + 1) + " where pes_cod=" + p.getCod();
                                Conexao.get().manipular(sql);
                            }
                        }
                    }
        }).start();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        launch(args);

    }

}
