/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rosolen.UI;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import rosolen.db.controladoras.CtrCategoria;
import rosolen.db.controladoras.CtrPessoa;
import rosolen.db.entidades.Pessoa;
import rosolen.db.entidades.Usuario;

/**
 * FXML Controller class
 *
 * @author thale
 */
public class UsuariosController implements Initializable
{

    public static final String FXML = "Usuarios.fxml";

    @FXML
    private HBox hb_procurar;
    @FXML
    private TextField txt_procurar;
    @FXML
    private Button bt_procurar;
    @FXML
    private TableColumn<?, ?> col_nome;
    @FXML
    private TextField txt_senha;
    @FXML
    private RadioButton rb_administrador;
    @FXML
    private ToggleGroup tg_cargo;
    @FXML
    private RadioButton rb_funcionario;
    @FXML
    private TableView<Pessoa> tabela;

    Pessoa selecionado;
    @FXML
    private Button bt_registrar;
    @FXML
    private AnchorPane todo;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        // TODO
        col_nome.setCellValueFactory(new PropertyValueFactory<>("nome"));

        txt_procurar.setOnKeyPressed((KeyEvent ke)
                ->
        {
            if (ke.getCode().equals(KeyCode.ENTER))
                evt_procurar();
        });

        bt_procurar.setOnAction((event) ->
        {
            evt_procurar();
        });
    }

    @FXML
    private void evt_selecionar(MouseEvent event)
    {
        if ((selecionado = tabela.getSelectionModel().getSelectedItem()) != null)
        {
            tg_cargo.getToggles().forEach((t) ->
            {
                ((Node) t).setDisable(false);
            });
            txt_senha.setDisable(false);
            bt_registrar.setDisable(false);
        } else
        {
            tg_cargo.getToggles().forEach((t) ->
            {
                ((Node) t).setDisable(true);
            });
            txt_senha.setDisable(true);
            bt_registrar.setDisable(true);
        }
    }

    @FXML
    private void evt_procurar()
    {
        String filtro = "";
        if (!txt_procurar.getText().trim().isEmpty())
            filtro = "nome ilike '%" + txt_procurar.getText() + "%'";
        ObservableList<Pessoa> ob = FXCollections.observableArrayList(new CtrPessoa().get(new Usuario(), filtro, "order by nome limit 20"));
        if (ob.size() > 0)
            tabela.setItems(ob);
        else
            tabela.setItems(null);
    }

    @FXML
    private void evt_registrar(ActionEvent event)
    {
        int categoria = 0;
        if (rb_administrador.isSelected())
            categoria = 1;
        if (rb_funcionario.isSelected())
            categoria = 2;
        ((Usuario) selecionado).setCategoria(new CtrCategoria().get(categoria));
        ((Usuario) selecionado).setSenha(txt_senha.getText().trim());

        new CtrPessoa().alterar(selecionado);
        todo.getChildren().clear();
    }

}
