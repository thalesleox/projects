/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rosolen.UI;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Accordion;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.view.JasperViewer;
import rosolen.db.util.Conexao;

/**
 * FXML Controller class
 *
 * @author thale
 */
public class TelaPrincipalController implements Initializable
{

    public static final String FXML = "TelaPrincipal.fxml";

    @FXML
    private BorderPane Todo;
    @FXML
    private ScrollPane PainelPrincipal;
    @FXML
    private HBox hbDados;
    @FXML
    private Accordion menuLateral;
    @FXML
    private Label menu_valores;
    @FXML
    private Label menu_usuarios;

    public static TelaPrincipalController instance;

    public TelaPrincipalController()
    {
        instance = this;
    }

    public static TelaPrincipalController getInstance()
    {
        return instance;
    }

    public Accordion getMenu()
    {
        return menuLateral;
    }

    public void setAreaTrabalho(String FXML)
    {
        try
        {
            Parent p = FXMLLoader.load(getClass().getResource(FXML));
            hbDados.getChildren().clear();
            hbDados.getChildren().addAll(p);
            rosolen.Rosolen.getInstance().ajustarTela();
        } catch (IOException ex)
        {
            Logger.getLogger(TelaPrincipalController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        if (rosolen.Rosolen.getInstance().getSessao().getCategoria().getCod() == 1)
        {
            menu_valores.setVisible(true);
            menu_valores.setDisable(false);
            menu_usuarios.setVisible(true);
            menu_usuarios.setDisable(false);
        } else
        {
            menu_valores.setVisible(false);
            menu_valores.setDisable(true);
            menu_usuarios.setVisible(false);
            menu_usuarios.setDisable(true);
        }
    }

    @FXML
    private void evt_FichaCadastral(MouseEvent event)
    {
        setAreaTrabalho(FichaCadastralController.FXML);
    }

    @FXML
    private void evt_Lancamentos(MouseEvent event)
    {
        setAreaTrabalho(LancamentosController.FXML);
    }

    @FXML
    private void evt_trocarUsuario(ActionEvent event)
    {
        rosolen.Rosolen.getInstance().Deslogar();
    }

    private void gerarRelatorio(String sql, String relat, String titulo)
    {
        try
        {
            //sql para obter os dados para o relatorio
            ResultSet rs = Conexao.get().consultar(sql);
            //implementação da interface JRDataSource para DataSource ResultSet
            JRResultSetDataSource jrRS = new JRResultSetDataSource(rs);
            //chamando o relatório
            String jasperPrint = JasperFillManager.fillReportToFile(relat, null, jrRS);
            JasperViewer viewer = new JasperViewer(jasperPrint, false, false);
            viewer.setExtendedState(JasperViewer.MAXIMIZED_BOTH);//maximizado
            viewer.setTitle(titulo);
            viewer.setVisible(true);
        } catch (JRException erro)
        {
            System.out.println(erro);
        }

        /*try {  //sql para obter os dados para o relatorio
            ResultSet rs = Banco.con.consultar(sql);
            //implementação da interface JRDataSource para DataSource ResultSet
            JRResultSetDataSource jrRS = new JRResultSetDataSource(rs);
            //preenchendo e chamando o relatório
            JasperPrint print = JasperFillManager.fillReport(relat, null, jrRS);
            // cria painel de visualização do relatório (usa Swing)
            JRViewer viewer = new JRViewer(print);
            viewer.setOpaque(true);
            viewer.setVisible(true);
            viewer.setZoomRatio(0.5f);

            //permite utilizar componente do swing no JavaFX
            SwingNode swingNode = new SwingNode();
            SwingUtilities.invokeLater(()->{swingNode.setContent(viewer);});

            // adicionando um painel swing no HBOX
            pndados.getChildren().clear();
            pndados.getChildren().add(swingNode);

        } catch (JRException erro) {
            System.out.println(erro);
        }*/
    }

    @FXML
    private void evt_backup(MouseEvent event)
    {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        if (Conexao.BackupRestore("backup.bat"))
            alert.setContentText("Backup realizado.");
        else
            alert.setContentText("Não foi possível realizar o backup.");

        alert.showAndWait();
    }

    @FXML
    private void evt_restore(MouseEvent event)
    {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        if (Conexao.BackupRestore("restore.bat"))
            alert.setContentText("Restore concluído.");
        else
            alert.setContentText("Não foi possivel efeturar o Restore do banco.");

        alert.showAndWait();
    }

    private void evt_historico(MouseEvent event)
    {
        setAreaTrabalho(HistoricoController.FXML);
    }

    @FXML
    private void evt_Valores(MouseEvent event)
    {
        setAreaTrabalho(ValoresController.FXML);
    }

    @FXML
    private void evt_Usuarios(MouseEvent event)
    {
        setAreaTrabalho(UsuariosController.FXML);
    }

    @FXML
    private void evt_aluno(MouseEvent event)
    {
        setAreaTrabalho(AlunosController.FXML);
    }
}
