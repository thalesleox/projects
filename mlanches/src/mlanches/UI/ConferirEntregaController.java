/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mlanches.UI;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

/**
 * FXML Controller class
 *
 * @author thale
 */
public class ConferirEntregaController implements Initializable
{

    @FXML
    private ComboBox<?> cb_entrega;
    @FXML
    private TextField txt_endereco;
    @FXML
    private TextField txt_total;
    @FXML
    private Button bt_add;
    @FXML
    private RadioButton rb_nao;
    @FXML
    private ToggleGroup gp_cancelar;
    @FXML
    private RadioButton rb_sim;
    @FXML
    private TextField txt_troco;
    @FXML
    private TextField txt_recebido;
    @FXML
    private TableView<?> tabela;
    @FXML
    private TableColumn<?, ?> col_nome;
    @FXML
    private TableColumn<?, ?> col_endereco;
    @FXML
    private Button bt_remove;
    @FXML
    private Button bt_confirmar;
    @FXML
    private Button bt_cancelar;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        // TODO
    }

}
