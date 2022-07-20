/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mlanches.UI;

import java.io.IOException;
import java.net.URL;
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
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import mlanches.db.controladoras.CtrCaixa;
import mlanches.db.entidades.Caixa;
import mlanches.db.util.Conexao;

/**
 * FXML Controller class
 *
 * @author thale
 */
public class TelaPrincipalController implements Initializable
{

    public static final String FXML = "TelaPrincipal.fxml";

    @FXML
    private HBox hbDados;
    @FXML
    private Accordion menuLateral;
    @FXML
    private BorderPane Todo;
    @FXML
    private ScrollPane PainelPrincipal;

    public static TelaPrincipalController instance;
    Caixa cx;
    @FXML
    private Label op_caixa;
    @FXML
    private Label op_mesa;
    @FXML
    private VBox hb_funcionalidades;
    @FXML
    private Label op_realizarPedido;
    @FXML
    private Label op_realizarRecebimento;
    @FXML
    private TitledPane mn_banco;

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

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        cx = new CtrCaixa().getUltimo();
        this.atualizarStatus(cx);
        if (mlanches.Mlanches.getInstance().Sessao.getCargo().getCod() == 0)
            mn_banco.setDisable(false);
        else
            mn_banco.setDisable(true);
    }

    public void atualizarStatus(Caixa cx)
    {
        switch (cx.getStatus())
        {
            case "aberto":
                hb_funcionalidades.getChildren().forEach((tp) ->
                {
                    tp.setDisable(false);
                });
                break;
            case "fechado":
                hb_funcionalidades.getChildren().forEach((tp) ->
                {
                    switch (((Label) tp).getText().toLowerCase())
                    {
                        case "caixa":
                            tp.setDisable(false);
                            break;
                        default:
                            tp.setDisable(true);
                    }
                });
                break;
        }
    }

    public void setAreaTrabalho(String FXML)
    {
        try
        {
            Parent p = FXMLLoader.load(getClass().getResource(FXML));
            hbDados.getChildren().clear();
            hbDados.getChildren().addAll(p);
            mlanches.Mlanches.getInstance().ajustarTela();
        } catch (IOException ex)
        {
            Logger.getLogger(TelaPrincipalController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void evt_trocarUsuario(ActionEvent event)
    {
        mlanches.Mlanches.getInstance().Deslogar();
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

    @FXML
    private void evt_pessoa(MouseEvent event)
    {
        setAreaTrabalho(CadastroPessoaController.FXML);
    }

    @FXML
    private void evt_mprima(MouseEvent event)
    {
        setAreaTrabalho(CadastroMateriaPrimaController.FXML);
    }

    @FXML
    private void evt_produto(MouseEvent event)
    {
        setAreaTrabalho(CadastroProdutoController.FXML);
    }

    @FXML
    private void evt_caixa(MouseEvent event)
    {
        setAreaTrabalho(CaixaController.FXML);
    }

    @FXML
    private void evt_mesa(MouseEvent event)
    {
        setAreaTrabalho(AbrirMesaController.FXML);
    }

    @FXML
    private void evt_realizarpedido(MouseEvent event)
    {
        setAreaTrabalho(RealizarPedidoController.FXML);
    }

    @FXML
    private void evt_realizarrecebimento(MouseEvent event)
    {
        setAreaTrabalho(RealizarRecebimentoController.FXML);
    }

    @FXML
    private void evt_pedidos(MouseEvent event)
    {
        /*gerarRelatorio("select pes.nome, ped.descricao, ped.obs, pes.endereco,ped.horasrealizado from (select * from pedido where pago is false and mesa <> 0) ped "
                + "inner join pessoa pes on ped.pes_cod = pes.pes_cod", "pedidosabertosmesa.jrxml", "Pedidos em Aberto.");*/
    }

    private void gerarRelatorio(String sql, String relat, String titulo)
    {
        /*relat = "relatorios/" + relat;
        try
        {
            //sql para obter os dados para o relatorio
            ResultSet rs = Conexao.get().consultar(sql);
            if (rs != null)
            {

                //implementação da interface JRDataSource para DataSource ResultSet
                JRResultSetDataSource jrRS = new JRResultSetDataSource(rs);
                JasperPrint jasperPrint = JasperFillManager.fillReport(relat, null, jrRS);
                JasperViewer viewer = new JasperViewer(jasperPrint);
                viewer.setExtendedState(JasperViewer.MAXIMIZED_BOTH);//maximizado
                viewer.setTitle(titulo);
                viewer.setVisible(true);
            }
        } catch (JRException erro)
        {
            System.out.println(erro);
        }

        try
        {  //sql para obter os dados para o relatorio
            ResultSet rs = Conexao.get().consultar(sql);
            //implementação da interface JRDataSource para DataSource ResultSet
            InputStream in = mlanches.Mlanches.getInstance().getClass().getResourceAsStream(relat);
            JasperReport jr = (JasperReport) JRLoader.loadObject(in);
            JRDataSource jrRS = new JRResultSetDataSource(rs);
            //preenchendo e chamando o relatório
            Map parameters = new HashMap();
            JasperPrint print = JasperFillManager.fillReport(jr, parameters, jrRS);
            // cria painel de visualização do relatório (usa Swing)
            JRViewer viewer = new JRViewer(print);
            viewer.setOpaque(true);
            viewer.setVisible(true);
            viewer.setZoomRatio(0.5f);
            //permite utilizar componente do swing no JavaFX
            SwingNode swingNode = new SwingNode();
            SwingUtilities.invokeLater(() ->
            {
                swingNode.setContent(viewer);
            });
            // adicionando um painel swing no HBOX
            HBox hb = new HBox();
            hb.setPrefHeight(PopupControl.USE_COMPUTED_SIZE);
            hb.setPrefWidth(PopupControl.USE_COMPUTED_SIZE);
            hb.getChildren().clear();
            hb.getChildren().add(swingNode);
            hbDados.getChildren().clear();
            hbDados.getChildren().addAll(hb);
            mlanches.Mlanches.getInstance().ajustarTela();
        } catch (JRException erro)
        {
            System.out.println(erro);
        }
        //gerando o jasper design
        JasperDesign JD = JRXmlLoader.load("src/mlanches/relatorios/" + relat);
        JasperReport relatorio = JasperCompileManager.compileReport(JD);
        ResultSet rs = Conexao.get().consultar(sql);
        JRResultSetDataSource jrRS = new JRResultSetDataSource(rs);
        Map parametros = new HashMap();
        parametros.put("nome", new String());
        parametros.put("descricao", new String());
        parametros.put("obs", new String());
        parametros.put("endereco", new String());
        JasperPrint impressao = JasperFillManager.fillReport(relatorio, parametros, jrRS);
        JasperViewer viewer = new JasperViewer(impressao, true);
        viewer.show();
        InputStream jasperTemplate = mlanches.Mlanches.instance.getClass().getResourceAsStream("relatorios/" + relat);
        System.out.println("oi");

        try
        {  //sql para obter os dados para o relatorio
            ResultSet rs = Conexao.get().consultar(sql);
            //implementação da interface JRDataSource para DataSource ResultSet
            JRResultSetDataSource jrRS = new JRResultSetDataSource(rs);
            //preenchendo e chamando o relatório
            JasperPrint print = JasperFillManager.fillReport("relatorios/" + relat, null, jrRS);
            // cria painel de visualização do relatório (usa Swing)
            JRViewer viewer = new JRViewer(print);
            viewer.setOpaque(true);
            viewer.setVisible(true);
            viewer.setZoomRatio(0.5f);

            //permite utilizar componente do swing no JavaFX
            SwingNode swingNode = new SwingNode();
            SwingUtilities.invokeLater(() ->
            {
                swingNode.setContent(viewer);
            });

            // adicionando um painel swing no HBOX
            HBox hb = new HBox();
            hb.setPrefHeight(PopupControl.USE_COMPUTED_SIZE);
            hb.setPrefWidth(PopupControl.USE_COMPUTED_SIZE);
            hb.getChildren().clear();
            hb.getChildren().add(swingNode);
            hbDados.getChildren().clear();
            hbDados.getChildren().addAll(hb);
            mlanches.Mlanches.getInstance().ajustarTela();

        } catch (JRException erro)
        {
            System.out.println(erro);
        }
        //gerando o jasper design
        JasperDesign JD = JRXmlLoader.load("src/mlanches/relatorios/" + relat);
        JasperReport relatorio = JasperCompileManager.compileReport(JD);
        ResultSet rs = Conexao.get().consultar(sql);

        JRResultSetDataSource jrRS = new JRResultSetDataSource(rs);

        Map parametros = new HashMap();
        parametros.put("nome", new String());
        parametros.put("descricao", new String());
        parametros.put("obs", new String());
        parametros.put("endereco", new String());
        JasperPrint impressao = JasperFillManager.fillReport(relatorio, parametros, jrRS);

        JasperViewer viewer = new JasperViewer(impressao, true);
        viewer.show();
                 InputStream jasperTemplate = mlanches.Mlanches.instance.getClass().getResourceAsStream("relatorios/" + relat);
        System.out.println("oi");*/
    }

    @FXML
    private void evt_ajuda(ActionEvent event)
    {
        try
        {

            String chmFile = "cmd.exe /c " + System.getProperty("user.dir") + "/lib/manualusuario.chm"; // veja esta linha
            Runtime run = Runtime.getRuntime();
            run.exec(chmFile);
        } catch (IOException ex)
        {
            Logger.getLogger(TelaPrincipalController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
