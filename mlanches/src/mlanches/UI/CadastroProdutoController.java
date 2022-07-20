/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mlanches.UI;

import java.net.URL;
import java.util.ArrayList;
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
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import mlanches.db.controladoras.CtrMateriaPrima;
import mlanches.db.controladoras.CtrProduto;
import mlanches.db.controladoras.CtrUnidadeMedida;
import mlanches.db.entidades.MateriaPrima;
import mlanches.db.entidades.Produto;
import mlanches.db.entidades.UnidadeMedida;
import mlanches.db.util.MaskFieldUtil;

/**
 * FXML Controller class
 *
 * @author thale
 */
public class CadastroProdutoController implements Initializable
{

    public static final String FXML = "CadastroProduto.fxml";
    private Produto selecionado;
    private int tipo;
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
    @FXML
    private VBox pn_procura;
    @FXML
    private ComboBox<UnidadeMedida> cb_umedida;
    @FXML
    private TextField txt_descricao;
    @FXML
    private TextField txt_valor;
    @FXML
    private TableView<Produto> tabelaProduto;
    @FXML
    private TextField txt_pIngrediente;
    @FXML
    private TableView<MateriaPrima> tabelaPesquisa;
    @FXML
    private TableView<MateriaPrima> tabelaIngrediente;
    @FXML
    private TableColumn<?, ?> col_prodDescricao;
    @FXML
    private TableColumn<?, ?> col_prodUmedida;
    @FXML
    private TableColumn<?, ?> col_prodValor;
    @FXML
    private TableColumn<?, ?> col_pesqDescricao;
    @FXML
    private TableColumn<?, ?> col_ingredienteDescricao;
    @FXML
    private TextField txt_prodProcura;
    @FXML
    private VBox pn_ingrediente;
    @FXML
    private VBox pn_pIngrediente;
    @FXML
    private Button bt_retirar;
    @FXML
    private Button bt_adicionar;
    @FXML
    private ToggleGroup gp_tipoProduto;
    @FXML
    private RadioButton rb_lanche;
    @FXML
    private RadioButton rb_pastel;
    @FXML
    private RadioButton rb_porcao;
    @FXML
    private RadioButton rb_outros;
    @FXML
    private RadioButton rb_pizza;
    @FXML
    private RadioButton rb_ingrediente;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        // TODO
        col_prodDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        col_prodUmedida.setCellValueFactory(new PropertyValueFactory<>("unidadeMedida"));
        col_prodValor.setCellValueFactory(new PropertyValueFactory<>("valor"));

        col_pesqDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));

        col_ingredienteDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));

        MaskFieldUtil.onlyAlfaNumericValue(this.txt_descricao);
        MaskFieldUtil.maxField(this.txt_descricao, 50);
        MaskFieldUtil.onlyAlfaNumericValue(this.txt_prodProcura);
        MaskFieldUtil.maxField(this.txt_prodProcura, 50);
        MaskFieldUtil.monetaryField(txt_valor);
        MaskFieldUtil.onlyAlfaNumericValue(this.txt_pIngrediente);
        MaskFieldUtil.maxField(this.txt_pIngrediente, 50);

        rb_lanche.setUserData("la");
        rb_pastel.setUserData("pa");
        rb_porcao.setUserData("po");
        rb_pizza.setUserData("pi");
        rb_ingrediente.setUserData("in");
        rb_outros.setUserData("ou");

        cb_umedida.getItems().addAll(new CtrUnidadeMedida().get(new UnidadeMedida(), "", "order by um_cod desc"));
        cb_umedida.getSelectionModel().selectFirst();
        tipo = 0;
        limparTela();

        txt_prodProcura.lengthProperty().addListener((event) ->
        {
            String filtro = "";
            if (!txt_prodProcura.getText().trim().isEmpty())
                filtro = "descricao ilike '%" + txt_prodProcura.getText() + "%'";
            ObservableList<Produto> ob = FXCollections.observableArrayList(new CtrProduto().get(new Produto(), filtro, "order by descricao limit 10"));
            if (ob.size() > 0)
                tabelaProduto.setItems(ob);
            else
                tabelaProduto.setItems(null);
        });

        txt_pIngrediente.lengthProperty().addListener((event) ->
        {
            String filtro = "";
            if (!txt_pIngrediente.getText().trim().isEmpty())
                filtro = "descricao ilike '%" + txt_pIngrediente.getText() + "%'";
            ObservableList<MateriaPrima> ob = FXCollections.observableArrayList(new CtrMateriaPrima().get(new MateriaPrima(), filtro, "order by descricao"));
            if (ob.size() > 0)
                tabelaPesquisa.setItems(ob);
            else
                tabelaPesquisa.setItems(null);
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
        pn_procura.setDisable(false);
        tabelaPesquisa.setDisable(false);
        bt_adicionar.setDisable(false);
        bt_retirar.setDisable(false);
        pn_pIngrediente.setDisable(false);
        pn_ingrediente.setDisable(true);

        txt_descricao.setText("");
        txt_valor.setText("0");
        txt_prodProcura.setText("");
        txt_pIngrediente.setText("");

        if (tabelaProduto.getItems() != null)
            tabelaProduto.getItems().clear();
        if (tabelaPesquisa.getItems() != null)
            tabelaPesquisa.getItems().clear();
        if (tabelaIngrediente.getItems() != null)
            tabelaIngrediente.getItems().clear();

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
        pn_procura.setDisable(true);
        cb_umedida.setDisable(false);
        pn_ingrediente.setDisable(false);
        txt_prodProcura.setText("");
        txt_pIngrediente.setText("");
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
        pn_dados.setDisable(true);
        cb_umedida.setDisable(false);
        pn_procura.setDisable(false);
        pn_ingrediente.setDisable(true);
        txt_prodProcura.setText("");
        txt_pIngrediente.setText("");
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
        pn_ingrediente.setDisable(true);
        txt_prodProcura.setText("");

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
        pn_ingrediente.setDisable(false);
        pn_pIngrediente.setDisable(true);
        bt_retirar.setDisable(true);
        txt_prodProcura.setText("");
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
            if (txt_valor.getText().trim().isEmpty())
            {
                alerta.setTitle("Valor Zero!");
                alerta.setContentText("O valor do produto não pode ser zero!");
                alerta.showAndWait();
                status = false;
            }
        }
        if (status)
            switch (tipo)
            {
                case 1:
                    status = new CtrProduto().salvar(getInfoTela());
                    break;
                case 2:
                    selecionado.setDescricao(txt_descricao.getText());
                    selecionado.setUnidadeMedida(cb_umedida.getSelectionModel().getSelectedItem());
                    selecionado.setValor(Float.parseFloat(txt_valor.getText().replace(',', '.')));
                    selecionado.setIngredientes(new ArrayList(tabelaIngrediente.getItems()));
                    status = new CtrProduto().alterar(selecionado);
                    break;
                case 3:
                    status = new CtrProduto().apagar(selecionado);
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
        selecionado = tabelaProduto.getSelectionModel().getSelectedItem();
        if (selecionado != null)
        {
            tabelaIngrediente.getItems().clear();
            pn_dados.setDisable(false);
            txt_valor.setText("0");
            if (tipo == 2)
            {
                pn_ingrediente.setDisable(false);
                pn_dados.setDisable(false);
            }
            setInfoTela(selecionado);
        }
    }

    private Produto getInfoTela()
    {
        return new Produto(txt_descricao.getText(), cb_umedida.getSelectionModel().getSelectedItem(), Float.parseFloat(txt_valor.getText().replace(',', '.')),
                new ArrayList<>(tabelaIngrediente.getItems()), (String) gp_tipoProduto.getSelectedToggle().getUserData());
    }

    private void setInfoTela(Produto p)
    {
        this.txt_descricao.setText(p.getDescricao());
        this.cb_umedida.getSelectionModel().select(p.getUnidadeMedida());
        this.txt_valor.setText("" + p.getValor() * 10);
        tabelaIngrediente.getItems().addAll(p.getIngredientes());
        switch (p.getTag())
        {
            case "la":
                rb_lanche.setSelected(true);
                break;
            case "po":
                rb_porcao.setSelected(true);
                break;
            case "pa":
                rb_pastel.setSelected(true);
                break;
            case "pi":
                rb_pizza.setSelected(true);
                break;
            case "in":
                rb_ingrediente.setSelected(true);
                break;
            case "ou":
                rb_outros.setSelected(true);
                break;
        }
    }

    @FXML
    private void evt_adicionar(ActionEvent event)
    {
        MateriaPrima mps = tabelaPesquisa.getSelectionModel().getSelectedItem();
        if (mps != null)
        {
            boolean achou = false;
            for (MateriaPrima mp : tabelaIngrediente.getItems())
                if (mp.getCod() == mps.getCod())
                    achou = true;
            if (!achou)
                tabelaIngrediente.getItems().add(mps);
        }

    }

    @FXML
    private void evt_remover(ActionEvent event)
    {
        MateriaPrima mps = tabelaIngrediente.getSelectionModel().getSelectedItem();
        if (mps != null)
            tabelaIngrediente.getItems().remove(mps);
    }
}
