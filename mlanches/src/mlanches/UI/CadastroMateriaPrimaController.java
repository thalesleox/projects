/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mlanches.UI;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import mlanches.db.controladoras.CtrMateriaPrima;
import mlanches.db.controladoras.CtrUnidadeMedida;
import mlanches.db.entidades.MateriaPrima;
import mlanches.db.entidades.UnidadeMedida;
import mlanches.db.util.MaskFieldUtil;

/**
 * FXML Controller class
 *
 * @author thale
 */
public class CadastroMateriaPrimaController implements Initializable
{

    public static final String FXML = "CadastroMateriaPrima.fxml";

    @FXML
    private HBox todo;
    @FXML
    private HBox hb_botoes;
    @FXML
    private Button bt_novo;
    @FXML
    private Button bt_alterar;
    @FXML
    private Button bt_apagar;
    @FXML
    private Button bt_verificar;
    @FXML
    private Button bt_confirmar;
    @FXML
    private Button bt_cancelar;
    @FXML
    private AnchorPane pn_dados;
    private int tipo;
    @FXML
    private VBox pn_procura;
    @FXML
    private TextField txt_procura;
    @FXML
    private TableView<MateriaPrima> tabela;
    @FXML
    private ComboBox<UnidadeMedida> cb_umedida;
    @FXML
    private TableColumn<?, ?> col_descricao;
    @FXML
    private TableColumn<?, ?> col_umedida;
    @FXML
    private TextField txt_descricao;

    private MateriaPrima selecionado;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        // TODO
        col_descricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        col_umedida.setCellValueFactory(new PropertyValueFactory<>("unidadeMedida"));

        MaskFieldUtil.onlyAlfaValue(this.txt_descricao);
        MaskFieldUtil.maxField(this.txt_descricao, 50);
        MaskFieldUtil.onlyAlfaValue(this.txt_procura);
        MaskFieldUtil.maxField(this.txt_procura, 50);

        cb_umedida.getItems().addAll(new CtrUnidadeMedida().get(new UnidadeMedida(), "", "order by um_cod desc"));
        cb_umedida.getSelectionModel().selectFirst();
        tipo = 0;
        limparTela();

        txt_procura.lengthProperty().addListener((event) ->
        {
            String filtro = "";
            if (!txt_procura.getText().trim().isEmpty())
                filtro = "descricao ilike '%" + txt_procura.getText() + "%'";
            ObservableList<MateriaPrima> ob = FXCollections.observableArrayList(new CtrMateriaPrima().get(new MateriaPrima(), filtro, "order by descricao limit 20"));
            if (ob.size() > 0)
                tabela.setItems(ob);
            else
                tabela.setItems(null);
        });
    }

    private void limparTela()
    {
        bt_novo.setDisable(false);
        bt_alterar.setDisable(false);
        bt_apagar.setDisable(false);
        bt_verificar.setDisable(false);
        bt_confirmar.setDisable(true);
        bt_cancelar.setDisable(true);
        pn_dados.setDisable(true);
        cb_umedida.setDisable(true);
        pn_procura.setDisable(true);

        txt_descricao.setText("");
        txt_procura.setText("");

        if (tabela.getItems() != null)
            tabela.getItems().clear();

        selecionado = null;

        tipo = 0;
    }

    @FXML
    private void evt_novo(ActionEvent event)
    {
        bt_novo.setDisable(true);
        bt_alterar.setDisable(true);
        bt_apagar.setDisable(true);
        bt_verificar.setDisable(true);
        bt_confirmar.setDisable(false);
        bt_cancelar.setDisable(false);
        pn_dados.setDisable(false);
        pn_dados.setDisable(false);
        cb_umedida.setDisable(false);
        tipo = 1;
    }

    @FXML
    private void evt_alterar(ActionEvent event)
    {
        bt_novo.setDisable(true);
        bt_alterar.setDisable(true);
        bt_apagar.setDisable(true);
        bt_verificar.setDisable(true);
        bt_confirmar.setDisable(false);
        bt_cancelar.setDisable(false);
        pn_dados.setDisable(false);
        cb_umedida.setDisable(false);
        pn_procura.setDisable(false);
        txt_procura.setText("");
        tipo = 2;
    }

    @FXML
    private void evt_apagar(ActionEvent event)
    {
        bt_novo.setDisable(true);
        bt_alterar.setDisable(true);
        bt_apagar.setDisable(true);
        bt_verificar.setDisable(true);
        bt_confirmar.setDisable(false);
        bt_cancelar.setDisable(false);
        pn_dados.setDisable(false);
        cb_umedida.setDisable(false);
        pn_procura.setDisable(false);
        txt_procura.setText("");
        tipo = 3;
    }

    @FXML
    private void evt_verificar(ActionEvent event)
    {
        bt_novo.setDisable(true);
        bt_alterar.setDisable(true);
        bt_apagar.setDisable(true);
        bt_verificar.setDisable(true);
        bt_confirmar.setDisable(false);
        bt_cancelar.setDisable(false);
        pn_dados.setDisable(false);
        pn_procura.setDisable(false);
        txt_procura.setText("");
    }

    @FXML
    private void evt_confirmar(ActionEvent event)
    {
        boolean status = true;
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.getButtonTypes().clear();
        alerta.getButtonTypes().add(ButtonType.OK);
        if (tipo != 0)
        {
            if (txt_descricao.getText().trim().isEmpty())
            {
                alerta.setTitle("Descrição vazia!");
                alerta.setContentText("A descrição não pode ser vazia!");
                alerta.showAndWait();
                status = false;
            }
            if (cb_umedida.getSelectionModel().getSelectedItem() == null)
            {
                alerta.setTitle("Unidade de medida errada!");
                alerta.setContentText("Escolha uma unidade de medida correta!");
                alerta.showAndWait();
                status = false;
            }
        }
        if (status)
            switch (tipo)
            {
                case 1:
                    status = new CtrMateriaPrima().salvar(getInfoTela());
                    break;
                case 2:
                    selecionado.setDescricao(txt_descricao.getText());
                    selecionado.setUnidadeMedida(cb_umedida.getSelectionModel().getSelectedItem());
                    status = new CtrMateriaPrima().alterar(selecionado);
                    break;
                case 3:
                    status = new CtrMateriaPrima().apagar(selecionado);
                    break;
            }

        if (status)
            limparTela();
    }

    @FXML
    private void evt_cancelar(ActionEvent event)
    {
        limparTela();
    }

    @FXML
    private void evt_click(MouseEvent event)
    {
        selecionado = tabela.getSelectionModel().getSelectedItem();
        if (selecionado != null)
        {
            pn_dados.setDisable(false);
            setInfoTela(selecionado);
        }
    }

    private MateriaPrima getInfoTela()
    {
        return new MateriaPrima(txt_descricao.getText(), cb_umedida.getSelectionModel().getSelectedItem());
    }

    private void setInfoTela(MateriaPrima mp)
    {
        this.txt_descricao.setText(mp.getDescricao());
        this.cb_umedida.getSelectionModel().select(mp.getUnidadeMedida());
    }
}
