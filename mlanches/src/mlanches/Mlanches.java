/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mlanches;

import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import mlanches.UI.LoginController;
import mlanches.UI.TelaPrincipalController;
import mlanches.db.controladoras.CtrCaixa;
import mlanches.db.entidades.Caixa;
import mlanches.db.entidades.Pessoa;
import mlanches.db.util.Conexao;

/**
 *
 * @author thale
 */
public class Mlanches extends Application
{

    private Stage Todo;
    public static Mlanches instance;

    public Mlanches()
    {
        instance = this;
    }

    public static Mlanches getInstance()
    {
        return instance;
    }

    public Pessoa Sessao;

    @Override
    public void start(Stage stage) throws Exception
    {
        if (Conexao.get().getEstadoConexao())
        {
            stage.getIcons().add(new Image(getClass().getResourceAsStream("imagens/logo1.png")));
            stage.setTitle("Mineirinho Lanches Business");
            Todo = stage;
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

    public Pessoa getSessao()
    {
        return this.Sessao;
    }

    public boolean Logar(boolean validacao, Pessoa user)
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
        Todo.setOnCloseRequest(event ->
        {
        });
        carregarLogin();
    }

    private void carregarPrograma()
    {
        try
        {
            Todo.setOnCloseRequest(event ->
            {
                Caixa cx = new CtrCaixa().get("select * from caixa order by cx_cod desc limit 1");
                if (cx.getStatus().equals("aberto"))
                {
                    Alert al = new Alert(Alert.AlertType.WARNING);
                    al.setHeaderText("O caixa ainda está aberto.");
                    al.setContentText("Deseja fechar o caixa e sair do programa ou voltar?");
                    al.getButtonTypes().clear();
                    al.getButtonTypes().add(new ButtonType("Fechar e sair", ButtonBar.ButtonData.YES));
                    al.getButtonTypes().add(new ButtonType("Voltar", ButtonBar.ButtonData.NO));
                    al.showAndWait();

                    switch (al.getResult().getButtonData())
                    {
                        case YES:
                            cx.setStatus("fechado");
                            cx.setData(LocalDateTime.now());
                            new CtrCaixa().salvar(cx);
                            break;
                        case NO:
                            event.consume();
                            break;
                    }
                }
            });
            setAreaTrabalho(TelaPrincipalController.FXML);

        } catch (Exception ex)
        {
            Logger.getLogger(Mlanches.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void carregarLogin()
    {
        try
        {
            setAreaTrabalho(LoginController.FXML);
        } catch (Exception ex)
        {
            Logger.getLogger(Mlanches.class.getName()).log(Level.SEVERE, null, ex);
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

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        launch(args);
    }

}
