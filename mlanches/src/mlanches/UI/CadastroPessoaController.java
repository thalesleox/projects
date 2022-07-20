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
import mlanches.db.controladoras.CtrCargo;
import mlanches.db.controladoras.CtrPessoa;
import mlanches.db.entidades.Cargo;
import mlanches.db.entidades.Pessoa;
import mlanches.db.util.MaskFieldUtil;

/**
 * FXML Controller class
 *
 * @author thale
 */
public class CadastroPessoaController implements Initializable
{

    public static final String FXML = "CadastroPessoa.fxml";

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
    private TextField txt_nome;
    @FXML
    private TextField txt_endereco;
    @FXML
    private TextField txt_telefone;
    @FXML
    private ComboBox<Cargo> cb_cargo;
    @FXML
    private AnchorPane pn_dados;
    private int tipo;
    @FXML
    private VBox pn_procura;
    @FXML
    private TextField txt_procura;
    @FXML
    private TableView<Pessoa> tabela;
    @FXML
    private TableColumn<?, ?> col_nome;
    @FXML
    private TableColumn<?, ?> col_endereco;
    @FXML
    private TableColumn<?, ?> col_telefone;
    @FXML
    private TableColumn<?, ?> col_cargo;

    private Pessoa selecionado;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        // TODO
        col_nome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        col_telefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));
        col_endereco.setCellValueFactory(new PropertyValueFactory<>("endereco"));
        col_cargo.setCellValueFactory(new PropertyValueFactory<>("cargo"));

        MaskFieldUtil.onlyAlfaValue(this.txt_nome);
        MaskFieldUtil.maxField(this.txt_nome, 50);
        MaskFieldUtil.onlyAlfaNumericValue(this.txt_endereco);
        MaskFieldUtil.maxField(this.txt_endereco, 50);
        MaskFieldUtil.onlyAlfaValue(this.txt_procura);
        MaskFieldUtil.maxField(this.txt_procura, 50);

        MaskFieldUtil.foneField(this.txt_telefone);
        cb_cargo.getItems().addAll(new CtrCargo().get(new Cargo(), "", "order by car_cod desc"));
        cb_cargo.getSelectionModel().selectFirst();
        tipo = 0;
        limparTela();

        txt_procura.lengthProperty().addListener((event) ->
        {
            String filtro = "";
            if (!txt_procura.getText().trim().isEmpty())
                filtro = "nome ilike '%" + txt_procura.getText() + "%'";
            ObservableList<Pessoa> ob = FXCollections.observableArrayList(new CtrPessoa().get(new Pessoa(), filtro, "order by nome limit 20"));
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
        cb_cargo.setDisable(true);
        pn_procura.setDisable(true);

        txt_nome.setText("");
        txt_endereco.setText("");
        txt_telefone.setText("");
        txt_procura.setText("");

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
        if (mlanches.Mlanches.getInstance().Sessao.getCargo().getCod() == 0)
            cb_cargo.setDisable(false);
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
        if (mlanches.Mlanches.getInstance().Sessao.getCargo().getCod() == 0)
            cb_cargo.setDisable(false);
        txt_procura.setText("");
        pn_procura.setDisable(false);
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
        if (mlanches.Mlanches.getInstance().Sessao.getCargo().getCod() == 0)
            cb_cargo.setDisable(false);
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
            if (txt_nome.getText().trim().isEmpty())
            {
                alerta.setTitle("Nome vazio!");
                alerta.setContentText("O nome não pode ser vazio!");
                alerta.showAndWait();
                status = false;
            }
            if (txt_endereco.getText().trim().isEmpty())
            {
                alerta.setTitle("Endereço vazio!");
                alerta.setContentText("O Endereço não pode ser vazio!");
                alerta.showAndWait();
                status = false;
            }
            if (txt_telefone.getText().trim().isEmpty())
            {
                alerta.setTitle("Telefone vazio!");
                alerta.setContentText("O telefone não pode ser vazio!");
                alerta.showAndWait();
                status = false;
            }
        }
        if (status)
            switch (tipo)
            {
                case 1:
                    status = new CtrPessoa().salvar(getInfoTela());
                    break;
                case 2:
                    selecionado.setNome(txt_nome.getText());
                    selecionado.setEndereco(txt_endereco.getText());
                    selecionado.setTelefone(txt_telefone.getText());
                    selecionado.setCargo(cb_cargo.getSelectionModel().getSelectedItem());
                    status = new CtrPessoa().alterar(selecionado);
                    break;
                case 3:
                    status = new CtrPessoa().apagar(selecionado);
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

    private Pessoa getInfoTela()
    {
        return new Pessoa(txt_nome.getText(), txt_endereco.getText(), txt_telefone.getText(), 0, cb_cargo.getSelectionModel().getSelectedItem());
    }

    private void setInfoTela(Pessoa p)
    {
        this.txt_nome.setText(p.getNome());
        this.txt_telefone.setText(p.getTelefone());
        this.txt_endereco.setText(p.getEndereco());
        this.cb_cargo.getSelectionModel().select(p.getCargo());
    }
}
