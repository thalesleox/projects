/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rosolen.UI;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import rosolen.db.controladoras.CtrPessoa;
import rosolen.db.entidades.Pessoa;

/**
 * FXML Controller class
 *
 * @author thale
 */
public class FamiliarController implements Initializable
{

    public static final String FXML = "Familiar.fxml";
    @FXML
    private BorderPane pn_procura;
    @FXML
    private TableColumn<?, ?> col_nome;
    @FXML
    private HBox hb_procurar;
    @FXML
    private TextField txt_procurar;
    @FXML
    private Button bt_procurar;
    @FXML
    private TableView<Pessoa> tabela2;
    @FXML
    private TableColumn<?, ?> col_nome2;
    @FXML
    private TableView<Pessoa> tabela1;
    @FXML
    private Button bt_add;
    @FXML
    private Button bt_remove;

    private Pessoa selecionado;
    @FXML
    private TableColumn<?, ?> col_nome1;
    @FXML
    private TableView<Pessoa> tabela3;
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
        col_nome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        col_nome1.setCellValueFactory(new PropertyValueFactory<>("nome"));
        col_nome2.setCellValueFactory(new PropertyValueFactory<>("nome"));

        ImageView iv1 = new ImageView(rosolen.Rosolen.getInstance().getImagem("imagens/add.png"));
        iv1.setPreserveRatio(true);
        iv1.setFitHeight(32);
        iv1.setFitHeight(32);
        bt_add.setGraphic(iv1);

        ImageView iv2 = new ImageView(rosolen.Rosolen.getInstance().getImagem("imagens/remove.png"));
        iv2.setPreserveRatio(true);
        iv2.setFitHeight(32);
        iv2.setFitHeight(32);
        bt_remove.setGraphic(iv2);

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

        bt_add.setOnAction((event) ->
        {
            if (tabela1.getSelectionModel().getSelectedItem() != null)
                for (Pessoa p : tabela2.getItems())
                {
                    FichaCadastralController.getInstance().alfamilia.add(p);
                    tabela3.getItems().add(p);
                }
        });

        bt_remove.setOnAction((event) ->
        {
            if (tabela3.getSelectionModel().getSelectedItem() != null)
            {
                FichaCadastralController.getInstance().aldelfamilia.add(tabela3.getSelectionModel().getSelectedItem());
                tabela3.getItems().remove(tabela3.getSelectionModel().getSelectedItem());
            }
        });

        bt_confirmar.setOnAction((event) ->
        {
            ((Stage) bt_confirmar.getScene().getWindow()).close();
        });

        bt_cancelar.setOnAction((event) ->
        {
            FichaCadastralController.getInstance().alfamilia.clear();
            FichaCadastralController.getInstance().aldelfamilia.clear();
            ((Stage) bt_confirmar.getScene().getWindow()).close();
        });

        Platform.runLater(() ->
        {
            if (bt_add.getScene().getUserData() != null)
                carregarTabela(tabela3, (Pessoa) bt_add.getScene().getUserData());
        });
    }

    @FXML
    private void evt_procurar()
    {
        String filtro = "";
        if (!txt_procurar.getText().trim().isEmpty())
            filtro = "nome ilike '%" + txt_procurar.getText() + "%'";
        ObservableList<Pessoa> ob = FXCollections.observableArrayList(new CtrPessoa().get(new Pessoa(), filtro, "order by nome limit 20"));
        if (ob.size() > 0)
            tabela1.setItems(ob);
        else
            tabela1.setItems(null);
    }

    private void carregarTabela(TableView<Pessoa> table, Pessoa P)
    {
        String sql = "select * from (select * from pessoa where familia<>0) p where pes_cod=$1 or familia=$1 or pes_cod=$2 or familia=$2";
        sql = sql.replace("$1", "" + P.getCod());
        sql = sql.replace("$2", "" + P.getFamilia());
        ObservableList<Pessoa> ob = FXCollections.observableArrayList(new CtrPessoa().get(new Pessoa(), sql));
        if (ob.size() > 0)
            table.setItems(ob);
        else
            table.setItems(null);
    }

    @FXML
    private void evt_selecionar1(MouseEvent event)
    {
        if ((selecionado = tabela1.getSelectionModel().getSelectedItem()) != null)
            carregarTabela(tabela2, selecionado);
    }
}
