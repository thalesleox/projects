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
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import mlanches.db.controladoras.CtrMesa;
import mlanches.db.controladoras.CtrPessoa;
import mlanches.db.entidades.Pessoa;
import mlanches.db.util.MaskFieldUtil;

/**
 * FXML Controller class
 *
 * @author thale
 */
public class AbrirMesaController implements Initializable
{

    public static final String FXML = "AbrirMesa.fxml";
    @FXML
    private TextField txt_cliente;
    @FXML
    private TableView<Pessoa> tabela;
    @FXML
    private TableColumn<?, ?> tc_nome;
    @FXML
    private TableColumn<?, ?> tc_endereco;
    @FXML
    private TableColumn<?, ?> tc_saldoDevedor;
    @FXML
    private TextField txt_nmesa;
    @FXML
    private AnchorPane todo;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {

        // TODO
        tc_nome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        tc_endereco.setCellValueFactory(new PropertyValueFactory<>("endereco"));
        tc_saldoDevedor.setCellValueFactory(new PropertyValueFactory<>("saldo"));

        MaskFieldUtil.onlyNumericValue(txt_nmesa);

        txt_cliente.setOnKeyPressed((KeyEvent ke)
                ->
        {
            if (ke.getCode().equals(KeyCode.ENTER))
                evt_procurar();
        });

        txt_cliente.lengthProperty().addListener((event) ->
        {
            evt_procurar();
        });

        evt_procurar();
    }

    @FXML
    private void evt_procurar()
    {
        String filtro = "";
        if (!txt_cliente.getText().trim().isEmpty())
            filtro = "nome ilike '%" + txt_cliente.getText() + "%'";
        ObservableList<Pessoa> ob = FXCollections.observableArrayList(new CtrPessoa().get(new Pessoa(), filtro, "order by nome limit 20"));
        if (ob.size() > 0)
            tabela.setItems(ob);
        else
            tabela.setItems(null);

    }

    @FXML
    private void evt_confirmar(ActionEvent event)
    {
        if (!txt_nmesa.getText().trim().isEmpty())
            if (tabela.getSelectionModel().getSelectedItem() != null)
            {
                CtrMesa ctrm = new CtrMesa();
                if (ctrm.abrir(Integer.parseInt(txt_nmesa.getText()), tabela.getSelectionModel().getSelectedItem().getCod()))
                    todo.setDisable(true);
                else
                    new Alert(Alert.AlertType.WARNING, ctrm.getErro(), ButtonType.OK).showAndWait();
            }
    }

    @FXML
    private void evt_cancelar(ActionEvent event)
    {
        txt_cliente.setText("");
        txt_nmesa.setText("");
    }
}
